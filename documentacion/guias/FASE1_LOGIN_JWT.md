# Fase 1 — Inicio de sesión con JWT y API protegida (paso a paso)

> Objetivo: que para usar la API haya que **iniciar sesión**, que el servidor devuelva un **token**, y que cada rol solo pueda hacer lo que le toca.
> Tiempo estimado: **una tarde (3–4 h)**.
> Todo el código de esta guía se ha probado en una copia de tu proyecto: compila y pasa las 10 pruebas del paso 12.

---

## Qué vas a construir (léelo antes de empezar)

Ahora mismo tu API está **abierta**: cualquiera puede llamar a `/api/trabajador` sin identificarse. Al terminar funcionará así:

```
1. La app manda email + contraseña  ──►  POST /auth/login
2. El servidor comprueba la contraseña (BCrypt) y responde con un TOKEN
3. La app guarda el token y lo manda en TODAS las peticiones:
        Authorization: Bearer eyJhbGciOi...
4. El servidor comprueba el token: si es válido y el rol tiene permiso → responde
                                    si no hay token / es falso / ha caducado → 401
                                    si el rol no tiene permiso → 403
```

**¿Qué es un token JWT?** Un texto con tres partes separadas por puntos: `cabecera.datos.firma`.
- En los **datos** van el email del trabajador, su **rol** y **cuándo caduca** (8 horas).
- La **firma** se calcula con una **clave secreta** que solo conoce el servidor. Si alguien cambia los datos (por ejemplo, se pone `ADMIN`), la firma deja de coincidir y el servidor lo rechaza.
- Ventaja: la app **no tiene que guardar la contraseña**, solo el token.

**Archivos que vas a tocar:**

| Acción | Archivo |
|---|---|
| ✏️ Modificar | `local.properties`, `application.properties` |
| 🗑️ Borrar | `controller/UsuariosController.java` (su función la hace `AuthController`) |
| ➕ Crear | `dto/LoginRequest.java`, `dto/LoginResponse.java` |
| ➕ Crear | `config/JwtConfig.java`, `config/OpenApiConfig.java` |
| ➕ Crear | `service/TokenService.java` |
| ✏️ Reescribir | `controller/AuthController.java`, `config/SecurityConfig.java` |
| ✏️ Modificar | `service/TrabajadorService.java` (un método nuevo), `controller/TrabajadorController.java` (quitar `//`) |

> 📁 Todas las rutas de clases son dentro de `backend/ERP-Imprenta/src/main/java/com/tfgLeilaFraileSimon/ERP_Imprenta/`.

---

## Paso 0 — Guarda lo que tienes antes de empezar

Tienes cambios sin subir (los DTOs de Trabajador, la paginación…). Guárdalos en una **rama** nueva, así si algo sale mal puedes volver atrás. Desde la carpeta del proyecto:

```bash
git checkout -b feature/login-jwt
```

```bash
git add -A
```

```bash
git commit -m "DTOs y paginación de Trabajador"
```

> Si VS Code te pregunta, también puedes hacerlo desde el panel de Git (icono de ramas a la izquierda).

---

## Paso 1 — La clave secreta y la duración del token

### 1.1 Genera una clave aleatoria
En la terminal:

```bash
openssl rand -base64 48
```

Te saldrá algo como `k3J9...` (64 caracteres). **Cópialo.**

### 1.2 Pégala en `backend/ERP-Imprenta/local.properties`
Este archivo **no se sube a GitHub** (está en el `.gitignore`), por eso la clave va aquí. Añade al final:

```properties
app.jwt.secreto=PEGA_AQUI_LA_CLAVE_QUE_HAS_GENERADO
```

> ⚠️ Mínimo 32 caracteres. Si la cambias, todos los tokens anteriores dejan de valer (es normal).

### 1.3 Añade la duración en `src/main/resources/application.properties`
Al final del archivo:

```properties
# Minutos que dura un token JWT antes de caducar (480 = 8 horas, una jornada)
app.jwt.minutos-validez=480
```

---

## Paso 2 — Borra `UsuariosController.java`

**Decisión:** en Sprinta solo inician sesión los **trabajadores** (cada uno con su rol). El login con token lo hace `AuthController` (paso 7), así que `UsuariosController` sobra. Además, tal como está no funciona: no tiene ninguna ruta y el `Jwt` que importa (`OAuth2ResourceServerProperties.Jwt`) es una clase de configuración, no el token.

Bórralo: `controller/UsuariosController.java` → clic derecho → **Eliminar**.

> 💡 Que los **clientes** de la imprenta puedan entrar a ver y aceptar sus presupuestos queda como **línea futura** (ya aparece así en la memoria). Si algún día se hace, se reutiliza este mismo sistema de tokens con un rol `CLIENTE`.

---

## Paso 3 — Los DTOs del login

### 3.1 `dto/LoginRequest.java` — lo que manda la app
```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/*
 * Datos que manda la aplicacion al iniciar sesion (POST /auth/login).
 */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,
        @NotBlank(message = "La contrasena es obligatoria")
        String password) {
}
```

### 3.2 `dto/LoginResponse.java` — lo que devuelve el servidor
```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

/*
 * Respuesta del login: el token, cuantos segundos dura y los datos del
 * trabajador (para que la app sepa su nombre y su rol sin otra peticion).
 */
public record LoginResponse(String token, long expiraEnSegundos, TrabajadorResponse trabajador) {
}
```

> 💡 Reutilizas tu `TrabajadorResponse`, que ya no lleva la contraseña.

---

## Paso 4 — `config/JwtConfig.java`: la "máquina" que firma y comprueba tokens

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

/*
 * Configuracion de los tokens JWT.
 *  - JwtEncoder: FIRMA los tokens nuevos (se usa en el login).
 *  - JwtDecoder: COMPRUEBA los tokens que llegan en cada peticion
 *    (firma correcta y que no haya caducado). Spring lo usa solo.
 * Los dos usan la misma clave secreta (app.jwt.secreto, en local.properties)
 * con el algoritmo HS256.
 */
@Configuration
public class JwtConfig {

    @Value("${app.jwt.secreto}")
    private String secreto;

    private SecretKey clave() {
        return new SecretKeySpec(secreto.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(clave()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(clave()).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
```

> No hace falta añadir nada al `pom.xml`: ya tienes `spring-boot-starter-security-oauth2-resource-server`, que trae todo esto.

---

## Paso 5 — `service/TokenService.java`: crea el token de un trabajador

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

/*
 * Genera el token JWT de un trabajador que acaba de iniciar sesion.
 * Dentro del token van:
 *  - subject: el email (quien es)
 *  - rol: su rol (que puede hacer)
 *  - issuedAt / expiresAt: cuando se creo y cuando caduca
 */
@Service
public class TokenService {
    private final JwtEncoder encoder;

    @Value("${app.jwt.minutos-validez:480}")
    private long minutosValidez;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generarToken(Trabajador trabajador) {
        Instant ahora = Instant.now();
        JwtClaimsSet datos = JwtClaimsSet.builder()
                .subject(trabajador.getEmail())
                .claim("rol", trabajador.getRol().name())
                .issuedAt(ahora)
                .expiresAt(ahora.plus(minutosValidez, ChronoUnit.MINUTES))
                .build();
        JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(cabecera, datos)).getTokenValue();
    }

    public long segundosValidez() {
        return minutosValidez * 60;
    }
}
```

> ⚠️ La línea de `JwsHeader` es necesaria: si no la pones, Spring intenta firmar con otro algoritmo (RS256) y da error.

---

## Paso 6 — Un método nuevo en `service/TrabajadorService.java`

Busca el método `obtener(Integer id)` y **justo encima** añade:

```java
    // Busca un trabajador por su email (se usa en GET /api/me)
    public Trabajador obtenerPorEmail(String email) {
        return repositorio.findByEmail(email).orElseThrow();
    }
```

---

## Paso 7 — Reescribe `controller/AuthController.java`

Borra todo lo que tiene y pon:

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LoginRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LoginResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.security.LoginAttemptService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.TokenService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.TrabajadorService;

import jakarta.validation.Valid;

/*
 * Inicio de sesion y "quien soy".
 *  - POST /auth/login (publico): comprueba email y contrasena y devuelve un token.
 *  - GET  /api/me (con token): devuelve los datos del trabajador que ha iniciado sesion.
 */
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TrabajadorService trabajadorService;
    private final LoginAttemptService loginAttemptService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
            TrabajadorService trabajadorService, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.trabajadorService = trabajadorService;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest datos) {
        // 1. Si ese email lleva 5 fallos seguidos, ni lo intentamos (429)
        if (loginAttemptService.estaBloqueado(datos.email())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Cuenta bloqueada por demasiados intentos fallidos. Intentalo en unos minutos.");
        }
        try {
            // 2. Spring comprueba email + contrasena (BCrypt) con TrabajadorService
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(datos.email(), datos.password()));
            // 3. Correcto: generamos el token
            Trabajador trabajador = (Trabajador) auth.getPrincipal();
            return new LoginResponse(tokenService.generarToken(trabajador), tokenService.segundosValidez(),
                    TrabajadorResponse.desde(trabajador));
        } catch (AuthenticationException e) {
            // Mismo mensaje si el email no existe o la contrasena esta mal (no damos pistas)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");
        }
    }

    @GetMapping("/api/me")
    public TrabajadorResponse yo(@AuthenticationPrincipal Jwt jwt) {
        // jwt.getSubject() es el email que metimos en el token al hacer login
        return TrabajadorResponse.desde(trabajadorService.obtenerPorEmail(jwt.getSubject()));
    }
}
```

---

## Paso 8 — Reescribe `config/SecurityConfig.java`

Es el archivo más importante. Sustitúyelo entero. **Los comentarios explican cada parte; reescríbelos con tus palabras** cuando lo entiendas.

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

/*
 * CONFIGURACION DE SEGURIDAD.
 *  - Contrasenas cifradas con BCrypt.
 *  - Login con email y contrasena en POST /auth/login, que devuelve un token JWT.
 *  - El resto de la API (/api/**) exige ese token en la cabecera
 *    "Authorization: Bearer <token>".
 *  - Sin sesion en el servidor (STATELESS): cada peticion lleva su token.
 *  - @EnableMethodSecurity activa los @PreAuthorize de los controladores.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.security.require-https:false}")
    private boolean exigirHttps;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Avisa con un evento de cada login correcto o fallido. LoginAttemptListener
    // los escucha para contar fallos y bloquear tras 5 intentos.
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publicador) {
        return new DefaultAuthenticationEventPublisher(publicador);
    }

    // El "comprobador" de email + contrasena que usa AuthController en el login.
    // Busca al trabajador con TrabajadorService (UserDetailsService) y compara
    // la contrasena con BCrypt. Le pasamos el publicador de eventos para que
    // el bloqueo por intentos fallidos siga funcionando.
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService usuarios, PasswordEncoder passwordEncoder,
            AuthenticationEventPublisher publicador) {
        DaoAuthenticationProvider proveedor = new DaoAuthenticationProvider(usuarios);
        proveedor.setPasswordEncoder(passwordEncoder);
        ProviderManager manager = new ProviderManager(proveedor);
        manager.setAuthenticationEventPublisher(publicador);
        return manager;
    }

    // Lee el rol que va DENTRO del token (claim "rol") y lo convierte en el
    // permiso de Spring. Sin prefijo "ROLE_", para que siga funcionando
    // @PreAuthorize("hasAuthority('ADMIN')").
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("rol");
        roles.setAuthorityPrefix("");
        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(roles);
        return conversor;
    }

    // Reglas de acceso:
    //  - /auth/login y la documentacion de Swagger: libres.
    //  - /api/**: hace falta un token valido.
    //  - Cualquier otra direccion: denegada.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Respuesta JSON cuando falta el token o no es valido (401)
        AuthenticationEntryPoint noAutenticado = (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"Tienes que iniciar sesion\"}");
        };

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/error").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(o -> o
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(noAutenticado))
                .exceptionHandling(e -> e.authenticationEntryPoint(noAutenticado));

        if (exigirHttps) {
            http.requiresChannel(canal -> canal.anyRequest().requiresSecure());
        }
        return http.build();
    }
}
```

> ⚠️ Cambia respecto a lo que tenías: ya **no hay `httpBasic`**. Tu web de prueba (`frontend/`) y el cliente Swing (`cliente/`) **dejarán de poder entrar**. Es lo esperado: los vas a sustituir (Fase 3 del plan).

---

## Paso 9 — Vuelve a proteger Trabajador

En `controller/TrabajadorController.java`, en los métodos `anadir`, `actualizar` y `eliminar`, **quita las dos barras** delante de `@PreAuthorize`:

```java
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
```

(lo mismo en `@PutMapping` y `@DeleteMapping`).

---

## Paso 10 — `config/OpenApiConfig.java`: el botón "Authorize" de Swagger

Para poder probar con token desde Swagger:

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/*
 * Documentacion de la API en Swagger (http://localhost:8080/swagger-ui.html).
 * Anade el boton "Authorize" para pegar el token y probar los endpoints protegidos.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "API de Sprinta", version = "1.0"),
        security = @SecurityRequirement(name = "token"))
@SecurityScheme(name = "token", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {
}
```

---

## Paso 11 — Compila y arranca

Desde `backend/ERP-Imprenta`:

```bash
./mvnw compile
```

Tiene que acabar en **BUILD SUCCESS**. Si sale un error, copia el mensaje y pregúntame.

```bash
./mvnw spring-boot:run
```

Espera a ver `Started ErpImprentaApplication`.

---

## Paso 12 — Prueba en Swagger (marca cada casilla)

Abre **http://localhost:8080/swagger-ui.html**

**Cómo usar el token en Swagger:**
1. Busca `POST /auth/login` → **Try it out** → escribe tu email y contraseña de admin → **Execute**.
2. Copia el valor de `"token"` de la respuesta (sin las comillas).
3. Arriba a la derecha, pulsa **Authorize** 🔓, pega el token y pulsa **Authorize** → **Close**.
4. Ahora todas las peticiones de Swagger llevan el token.

| # | Prueba | Resultado esperado | ✔ |
|---|---|---|---|
| 1 | `GET /api/trabajador` **sin** pulsar Authorize | **401** | ☐ |
| 2 | Login con contraseña incorrecta | **401** "Email o contraseña incorrectos" | ☐ |
| 3 | Login correcto | **200** con `token` | ☐ |
| 4 | `GET /api/me` con el token | **200** con tus datos y `"rol": "ADMIN"` | ☐ |
| 5 | `GET /api/trabajador` con el token | **200** | ☐ |
| 6 | Como admin, `POST /api/trabajador` creando un **OPERARIO** (contraseña ≥ 8) | **200** | ☐ |
| 7 | Haz login con ese operario, pon **su** token en Authorize e intenta crear otro trabajador | **403** (no tiene permiso) | ☐ |
| 8 | En Authorize, cambia una letra del token y llama a `/api/me` | **401** | ☐ |
| 9 | Abre http://localhost:8080/v3/api-docs | **200** (se ve un JSON) | ☐ |
| 10 | 5 logins fallidos seguidos con el operario y luego el login correcto | **429** "Cuenta bloqueada…" (espera 15 min o reinicia el servidor) | ☐ |

> ¿Olvidaste la contraseña del admin? Si es la inicial, es la de `app.admin-inicial.password` en `application.properties` (o la variable `ADMIN_PASSWORD`).

---

## Paso 13 — Guarda el trabajo

```bash
git add -A
```

```bash
git commit -m "Login con JWT, endpoint /api/me y permisos por rol en Trabajador"
```

Cuando todas las pruebas pasen, une la rama a `main`:

```bash
git checkout main
```

```bash
git merge feature/login-jwt
```

```bash
git push
```

---

## Para la memoria y la defensa (apúntalo)

- **Qué has hecho:** sustituir la autenticación básica por **JWT**, añadir `POST /auth/login` y `GET /api/me`, proteger toda `/api/**` y reactivar los permisos de administrador.
- **Por qué JWT:** la app no guarda la contraseña; el token caduca; lleva el rol dentro; es lo estándar para apps de escritorio y móvil.
- **Preguntas que te pueden hacer:**
  - *¿Dónde está la clave secreta?* En `local.properties`, que no se sube a GitHub.
  - *¿Qué pasa si alguien cambia el rol dentro del token?* La firma deja de coincidir y el servidor responde 401.
  - *¿Qué diferencia hay entre 401 y 403?* 401 = no sé quién eres (sin token o token inválido). 403 = sé quién eres pero tu rol no puede hacer eso.
  - *¿Cómo funciona el bloqueo?* `LoginAttemptService` cuenta los fallos por email; con 5 seguidos bloquea 15 minutos.
- Cuando termines, la frase de la página de **Implementación** de la memoria sobre `@PreAuthorize` pasa a ser verdad. Avísame y actualizo ese capítulo con lo que has hecho.

## Lo siguiente (Fase 2)

Gestor de errores común (para que un id que no existe dé **404** y no 500), DTOs para el resto de entidades y permisos por rol en todos los controladores.
