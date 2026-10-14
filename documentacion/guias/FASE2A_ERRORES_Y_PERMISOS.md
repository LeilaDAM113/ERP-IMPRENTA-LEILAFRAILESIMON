# Fase 2A — Errores claros y permisos por rol en toda la API (paso a paso)

> Objetivo: que la API **explique bien los errores** (404, 400, 409…) en vez de dar siempre un 500, y que **cada rol solo pueda hacer lo que le toca** en todos los controladores, no solo en Trabajador.
> Tiempo estimado: **una tarde (3–4 h)**.
> Todo el código de esta guía está probado en una copia de tu proyecto: compila y pasa las 21 pruebas del paso 7.

---

## Qué vas a arreglar (léelo antes de empezar)

**Problema 1 — Errores que no dicen nada.** Ahora mismo:

| Si haces… | Te sale | Debería salir |
|---|---|---|
| `GET /api/cliente/99999` (no existe) | 500 «Internal Server Error» | **404** «No existe ningún registro con ese id» |
| `PUT /api/cliente/99999` | **¡Crea un cliente nuevo!** | **404** |
| `POST` con `"rol": "Operario"` | 400 con nombres raros de clases Java | **400** «Valores permitidos: [ADMIN, COMERCIAL…]» |
| Crear un trabajador con un email que ya existe | 500 | **409** «Hay un dato repetido…» |
| Borrar un cliente que tiene presupuestos | 500 | **409** «…el registro lo está usando otro» |

**Problema 2 — Permisos.** Ahora solo Trabajador está protegido por rol. Un operario puede borrar facturas o clientes. Vamos a aplicar la **matriz de permisos de la memoria** a toda la API.

**Archivos que vas a tocar:**

| Acción | Archivo |
|---|---|
| ➕ Crear | `dto/RespuestaError.java` |
| ➕ Crear (carpeta nueva `exception`) | `exception/GlobalExceptionHandler.java` |
| ✏️ Modificar | Los **21 servicios**: métodos `actualizar` y `eliminar` |
| ✏️ Modificar | `config/SecurityConfig.java` |

> 📁 Rutas dentro de `backend/ERP-Imprenta/src/main/java/com/tfgLeilaFraileSimon/ERP_Imprenta/`.
> ⚠️ Recuerda: al crear un archivo nuevo, VS Code escribe solo la línea `package`. **Bórrala antes de pegar** (o no copies la primera línea), para que no salga repetida.

---

## Paso 0 — Rama nueva

```bash
git checkout -b feature/errores-y-permisos
```

---

## Paso 1 — `dto/RespuestaError.java`: cómo se ven todos los errores

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDateTime;
import java.util.Map;

// Asi es como devuelve la API todos los errores, siempre igual,
// para que las apps puedan enseñar el mensaje al usuario.
// errores = el fallo de cada campo del formulario (si lo hay)
public record RespuestaError(int estado, String mensaje, Map<String, String> errores, String ruta,
        LocalDateTime fecha) {
}
```

Un error se verá así:
```json
{
  "estado": 404,
  "mensaje": "No existe ningun registro con ese id",
  "errores": null,
  "ruta": "/api/cliente/99999",
  "fecha": "2026-10-11T13:57:25"
}
```

---

## Paso 2 — `exception/GlobalExceptionHandler.java`: el que recoge todos los errores

1. Clic derecho en la carpeta `ERP_Imprenta` → **New Folder** → `exception`.
2. Dentro, crea `GlobalExceptionHandler.java`:

```java
package com.tfgLeilaFraileSimon.ERP_Imprenta.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.RespuestaError;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

// Aqui llegan todos los errores de la API.
// En vez de devolver siempre un error 500 que no dice nada,
// cada tipo de error devuelve su codigo y un mensaje que se entienda.
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Monto la respuesta de error siempre con el mismo formato
    private ResponseEntity<RespuestaError> respuesta(HttpStatusCode estado, String mensaje,
            Map<String, String> errores, HttpServletRequest peticion) {
        RespuestaError cuerpo = new RespuestaError(estado.value(), mensaje, errores,
                peticion.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.status(estado).body(cuerpo);
    }

    // Piden un id que no existe -> 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RespuestaError> noEncontrado(NoSuchElementException e, HttpServletRequest peticion) {
        return respuesta(HttpStatus.NOT_FOUND, "No existe ningun registro con ese id", null, peticion);
    }

    // Algun campo no cumple las reglas (@NotBlank, @Email...) -> 400
    // y digo que campo falla para que la app lo pueda marcar en rojo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> datosNoValidos(MethodArgumentNotValidException e,
            HttpServletRequest peticion) {
        Map<String, String> errores = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "Hay datos que no son validos", errores, peticion);
    }

    // El JSON viene mal escrito o con un valor raro -> 400
    // (por ejemplo "rol": "Operario" en vez de "OPERARIO")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError> jsonNoValido(HttpMessageNotReadableException e,
            HttpServletRequest peticion) {
        if (e.getMostSpecificCause() instanceof InvalidFormatException formato) {
            String campo = formato.getPath().isEmpty() ? "dato"
                    : formato.getPath().get(formato.getPath().size() - 1).getPropertyName();
            String mensaje = "El valor '" + formato.getValue() + "' no es valido para " + campo;
            // si es un enum (rol, estado...) le digo que valores puede poner
            if (formato.getTargetType() != null && formato.getTargetType().isEnum()) {
                mensaje += ". Valores permitidos: " + Arrays.toString(formato.getTargetType().getEnumConstants());
            }
            return respuesta(HttpStatus.BAD_REQUEST, mensaje, Map.of(campo, mensaje), peticion);
        }
        return respuesta(HttpStatus.BAD_REQUEST,
                "El JSON no es valido (revisa comillas, comas, numeros y fechas AAAA-MM-DD)", null, peticion);
    }

    // En la URL ponen algo que no es un numero, por ejemplo /api/cliente/abc -> 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespuestaError> parametroNoValido(MethodArgumentTypeMismatchException e,
            HttpServletRequest peticion) {
        return respuesta(HttpStatus.BAD_REQUEST, "El valor '" + e.getValue() + "' no es valido para " + e.getName(),
                null, peticion);
    }

    // La base de datos no deja guardar o borrar -> 409
    // pasa con un email repetido o al borrar algo que se esta usando
    // (por ejemplo un cliente que ya tiene presupuestos)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaError> conflicto(DataIntegrityViolationException e,
            HttpServletRequest peticion) {
        return respuesta(HttpStatus.CONFLICT,
                "No se puede guardar o borrar: hay un dato repetido o el registro lo esta usando otro", null,
                peticion);
    }

    // El rol no tiene permiso para hacer eso -> 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RespuestaError> sinPermiso(AccessDeniedException e, HttpServletRequest peticion) {
        return respuesta(HttpStatus.FORBIDDEN, "No tienes permiso para hacer esta operacion", null, peticion);
    }

    // Cualquier otro error.
    // Si ya trae su codigo (por ejemplo el 401 y el 429 del login, o un 405)
    // respeto ese codigo. Si no, es un fallo que no esperaba -> 500,
    // lo apunto en la consola y al usuario no le enseño detalles internos.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> otroError(Exception e, HttpServletRequest peticion) {
        if (e instanceof ErrorResponse conCodigo) {
            return respuesta(conCodigo.getStatusCode(), conCodigo.getBody().getDetail(), null, peticion);
        }
        log.error("Error inesperado en {}", peticion.getRequestURI(), e);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado en el servidor", null,
                peticion);
    }
}
```

**Cómo funciona (para entenderlo y explicarlo):**
- `@RestControllerAdvice` = «esta clase vigila todos los controladores».
- Cada `@ExceptionHandler(X.class)` = «cuando salte un error de tipo X, haz esto».
- Tus servicios ya usan `orElseThrow()`, que lanza `NoSuchElementException` cuando no encuentra el id. Ahora ese error se convierte en **404** sin tocar nada más.
- Los mensajes no llevan tildes a propósito, igual que los demás mensajes de tu API.

---

## Paso 3 — Arregla `actualizar` y `eliminar` en los servicios

**El fallo:** en los servicios genéricos (Cliente, Albaran, Factura…), `actualizar` hace `objeto.setId(id)` y `save`. Si ese id **no existe**, `save` **crea un registro nuevo**. Y `eliminar` con un id que no existe no avisa de nada.

**El arreglo:** comprobar antes si existe. Hazlo **primero en `ClienteService.java`** para entenderlo:

1. Arriba, junto a `import java.util.List;`, añade:
   ```java
   import java.util.NoSuchElementException;
   ```
2. Sustituye los métodos `actualizar` y `eliminar` por estos:
   ```java
       // cambiar los datos de uno que ya existe
       public Cliente actualizar(Integer id, Cliente objeto) {
           // si no existe ese id, error 404 (antes creaba uno nuevo sin querer)
           if (!repositorio.existsById(id)) {
               throw new NoSuchElementException();
           }
           objeto.setId(id);
           return repositorio.save(objeto);
       }

       // borrar uno
       public void eliminar(Integer id) {
           // si no existe ese id, error 404
           if (!repositorio.existsById(id)) {
               throw new NoSuchElementException();
           }
           repositorio.deleteById(id);
       }
   ```

**Ahora repítelo en el resto.** Es lo mismo, cambiando `Cliente` por la clase de cada servicio. Marca cada uno:

☐ Albaran ☐ Catalogo ☐ Cliente ☐ ContactoEmpresa ☐ Empresa ☐ Factura ☐ Inventario ☐ LineaAlbaran ☐ LineaFactura ☐ LineaPresupuesto ☐ Maquinaria ☐ OrdenEmpleado ☐ OrdenTrabajador ☐ OrdenTrabajo ☐ Pago ☐ Particular ☐ Pedido ☐ Presupuesto ☐ Proveedor ☐ Puesto

**En `TrabajadorService.java`** solo cambia `eliminar` (su `actualizar` ya usa `findById(id).orElseThrow()`, que da 404), y añade el mismo import:
```java
    public void eliminar(Integer id) {
        // si no existe ese id, error 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }
```

> 💡 **Truco para ir más rápido:** en VS Code, con `Cmd + Shift + F` buscas `objeto.setId(id);` y te salen los 20 sitios. Ve abriendo uno a uno.

---

## Paso 4 — Permisos por rol en `config/SecurityConfig.java`

Vas a hacer **tres cambios** en este archivo.

### 4.1 Imports nuevos
Junto a los demás imports (el de `Customizer` ya lo tienes si cambiaste lo del HTTPS):
```java
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
```

### 4.2 Los nombres de los roles (sacados de tu enum `RolTrabajador`)
Justo debajo de `public class SecurityConfig {` añade:
```java
    // Los roles los saco del enum RolTrabajador, asi no los escribo a mano
    // y si algun dia cambio un rol, el compilador me avisa aqui.
    // .name() da el nombre del rol como texto ("ADMIN", "COMERCIAL"...)
    private static final String ADMIN = RolTrabajador.ADMIN.name();
    private static final String COMERCIAL = RolTrabajador.COMERCIAL.name();
    private static final String ENCARGADO = RolTrabajador.ENCARGADO_TALLER.name();
    private static final String OPERARIO = RolTrabajador.OPERARIO.name();
    private static final String TRANSPORTISTA = RolTrabajador.TRANSPORTISTA.name();
```

> **¿Por qué `.name()`?** `hasAnyAuthority(...)` pide textos (`String`), no enums. `.name()` convierte `RolTrabajador.ADMIN` en el texto `"ADMIN"`, que es justo lo que va dentro del token.

### 4.3 Sustituye el método `securityFilterChain` entero por este
```java
    // Quien puede entrar a cada sitio
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // lo que se responde si no hay token o el token no vale (401)
        AuthenticationEntryPoint noAutenticado = (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"Tienes que iniciar sesion\"}");
        };

        // lo que se responde si el rol no tiene permiso (403)
        AccessDeniedHandler sinPermiso = (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"No tienes permiso para hacer esta operacion\"}");
        };

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/error").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/me").authenticated()

                        // ---- PERMISOS POR ROL ----
                        // Primero digo quien puede CONSULTAR (GET) y luego
                        // quien puede crear, modificar y borrar (todo lo demas).

                        // Trabajadores y puestos: los ve el encargado, los gestiona el admin
                        .requestMatchers(HttpMethod.GET, "/api/trabajador/**", "/api/puesto/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)
                        .requestMatchers("/api/trabajador/**", "/api/puesto/**").hasAuthority(ADMIN)

                        // Clientes: los gestiona el comercial
                        .requestMatchers(HttpMethod.GET, "/api/cliente/**", "/api/empresa/**",
                                "/api/particular/**", "/api/contacto-empresa/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, TRANSPORTISTA)
                        .requestMatchers("/api/cliente/**", "/api/empresa/**", "/api/particular/**",
                                "/api/contacto-empresa/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)

                        // Catalogo de productos: lo cambia solo el admin
                        .requestMatchers(HttpMethod.GET, "/api/catalogo/**").hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO)
                        .requestMatchers("/api/catalogo/**").hasAuthority(ADMIN)

                        // Presupuestos: los hace el comercial
                        .requestMatchers(HttpMethod.GET, "/api/presupuesto/**", "/api/linea-presupuesto/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO)
                        .requestMatchers("/api/presupuesto/**", "/api/linea-presupuesto/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)

                        // Ordenes de trabajo: las organiza el encargado, todos las pueden ver
                        .requestMatchers(HttpMethod.GET, "/api/orden-trabajo/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, OPERARIO, TRANSPORTISTA)
                        .requestMatchers("/api/orden-trabajo/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // Operarios asignados a cada orden
                        .requestMatchers(HttpMethod.GET, "/api/orden-trabajador/**", "/api/orden-empleado/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/orden-trabajador/**", "/api/orden-empleado/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)

                        // Maquinaria: la gestiona el encargado, el operario solo la ve
                        .requestMatchers(HttpMethod.GET, "/api/maquinaria/**").hasAnyAuthority(ADMIN, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/maquinaria/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // Almacen, compras y proveedores: los gestiona el encargado
                        .requestMatchers(HttpMethod.GET, "/api/inventario/**", "/api/pedido/**", "/api/proveedor/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/inventario/**", "/api/pedido/**", "/api/proveedor/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)

                        // Albaranes: los ve el transportista, los hacen el admin y el encargado
                        .requestMatchers(HttpMethod.GET, "/api/albaran/**", "/api/linea-albaran/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, TRANSPORTISTA)
                        .requestMatchers("/api/albaran/**", "/api/linea-albaran/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // Facturas y pagos: solo el admin (el comercial puede consultarlas)
                        .requestMatchers(HttpMethod.GET, "/api/factura/**", "/api/linea-factura/**", "/api/pago/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)
                        .requestMatchers("/api/factura/**", "/api/linea-factura/**", "/api/pago/**")
                            .hasAuthority(ADMIN)

                        // Cualquier otra direccion de la API que no este en la lista: prohibida.
                        // Asi, si creo un controlador nuevo y se me olvida ponerlo aqui,
                        // no queda abierto para todo el mundo.
                        .requestMatchers("/api/**").denyAll()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(o -> o
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(noAutenticado))
                .exceptionHandling(e -> e.authenticationEntryPoint(noAutenticado).accessDeniedHandler(sinPermiso));

        // si en application.properties pongo que hay que usar HTTPS,
        // las peticiones que lleguen por http:// se mandan a https://
        if (exigirHttps) {
            http.redirectToHttps(Customizer.withDefaults());
        }
        return http.build();
    }
```

**Cómo leerlo:**
- Spring mira las reglas **de arriba abajo** y aplica **la primera que coincide**. Por eso en cada bloque va primero el `GET` (consultar) y después la regla sin método (crear, modificar y borrar).
- `/**` significa «esta dirección y todo lo que va detrás» (por ejemplo `/api/cliente/5`).
- La última regla `/api/**` → `denyAll()` cierra todo lo que no esté en la lista. **Cuando crees un controlador nuevo, tendrás que añadir aquí su regla**, o dará 403.

**Resumen de permisos (está en la memoria, apartado «Matriz de permisos por rol»):**

| Módulo | Admin | Comercial | Encargado | Operario | Transportista |
|---|---|---|---|---|---|
| Trabajadores, puestos | Todo | — | Ver | — | — |
| Clientes (y empresas, particulares, contactos) | Todo | Todo | Ver | — | Ver |
| Catálogo | Todo | Ver | Ver | — | — |
| Presupuestos | Todo | Todo | Ver | — | — |
| Órdenes de trabajo | Todo | Ver | Todo | Ver | Ver |
| Operarios de cada orden | Todo | — | Todo | Ver | — |
| Maquinaria | Todo | — | Todo | Ver | — |
| Inventario, pedidos, proveedores | Todo | Ver | Todo | Ver | — |
| Albaranes | Todo | Ver | Todo | — | Ver |
| Facturas y pagos | Todo | Ver | — | — | — |

> Más adelante (fase 2D), el operario podrá registrar **sus** tiempos y material, y el transportista **firmar sus** albaranes, con endpoints especiales.

---

## Paso 5 — Compila

```bash
./mvnw compile
```

Tiene que acabar en **BUILD SUCCESS**. Si sale un error, cópiamelo.

---

## Paso 6 — Prepara los usuarios de prueba

Arranca la API (`./mvnw spring-boot:run`) y, en Swagger, como **ADMIN**:
1. Crea un **comercial** con `POST /api/trabajador`:
   ```json
   {
     "nombreCompleto": "Laura Gomez Ruiz",
     "email": "laura.gomez@prueba.es",
     "password": "comercial2026",
     "activo": true,
     "rol": "COMERCIAL"
   }
   ```
2. Ya tienes un **operario** de la fase 1 (`operario@prueba.es` / `holaquetal`).

Recuerda: para cambiar de usuario, **Authorize → Logout**, login con el otro y pegar su token.

---

## Paso 7 — Pruebas en Swagger (marca cada casilla)

### Errores (con el token del admin)

| # | Prueba | Esperado | ✔ |
|---|---|---|---|
| 1 | `GET /api/cliente/{id}` con id `99999` | **404** «No existe ningun registro con ese id» | ☐ |
| 2 | `PUT /api/cliente/{id}` con id `99999` y `{"nombreCompleto":"X"}` | **404** (ya no crea uno nuevo) | ☐ |
| 3 | `DELETE /api/cliente/{id}` con id `99999` | **404** | ☐ |
| 4 | `POST /api/trabajador` con `{"email":"sinarroba","password":"123"}` | **400** con `errores` de `email` y `password` | ☐ |
| 5 | `POST /api/trabajador` con `"rol": "Operario"` | **400** «…Valores permitidos: [ADMIN, …]» | ☐ |
| 6 | `POST /api/trabajador` con el email de Laura otra vez | **409** «hay un dato repetido…» | ☐ |
| 7 | `POST /api/trabajador` con el JSON mal cerrado: `{"email": ` | **400** «El JSON no es valido…» | ☐ |
| 8 | Login con contraseña mal | **401** «Email o contraseña incorrectos» | ☐ |
| 9 | Crea un cliente, crea un presupuesto con `"cliente": {"id": <ese id>}` y luego borra el cliente | **409** «…lo esta usando otro» | ☐ |

### Permisos

| # | Usuario | Prueba | Esperado | ✔ |
|---|---|---|---|---|
| 10 | Operario | `GET /api/factura` | **403** | ☐ |
| 11 | Operario | `GET /api/maquinaria` | **200** | ☐ |
| 12 | Operario | `POST /api/maquinaria` con `{"nombre":"X"}` | **403** | ☐ |
| 13 | Operario | `GET /api/orden-trabajo` | **200** | ☐ |
| 14 | Comercial | `POST /api/cliente` con `{"nombreCompleto":"Cliente prueba","tipoCliente":"PARTICULAR"}` | **200** | ☐ |
| 15 | Comercial | `POST /api/maquinaria` | **403** | ☐ |
| 16 | Comercial | `GET /api/trabajador` | **403** | ☐ |
| 17 | Comercial | `GET /api/factura` | **200** | ☐ |
| 18 | Comercial | `POST /api/factura` con `{}` | **403** | ☐ |
| 19 | Admin | `GET /api/factura` | **200** | ☐ |
| 20 | Cualquiera | `GET /api/me` | **200** | ☐ |
| 21 | Sin token | `GET /api/cliente` | **401** | ☐ |

---

## Paso 8 — Guarda el trabajo

```bash
git add -A
```

```bash
git commit -m "Errores claros (404, 400, 409) y permisos por rol en toda la API"
```

```bash
git checkout main
```

```bash
git merge feature/errores-y-permisos
```

```bash
git push
```

---

## Para la memoria y la defensa

- **Gestor de errores global:** una sola clase (`@RestControllerAdvice`) convierte cada tipo de error en su código HTTP y un mensaje entendible, siempre con el mismo formato. Así las apps pueden enseñar el mensaje y marcar en rojo el campo que falla.
- **Por qué 404 y no 500:** 500 significa «el servidor ha fallado»; 404 significa «lo que buscas no existe». Es un error del usuario, no del servidor.
- **Qué es un 409:** conflicto con los datos que ya hay (un email repetido o borrar un cliente que tiene presupuestos). La base de datos protege la integridad gracias a las claves ajenas.
- **Permisos centralizados:** todos los permisos están en un único sitio (`SecurityConfig`), en forma de tabla. Es fácil de revisar y coincide con la matriz de permisos de la memoria.
- **Cerrado por defecto:** cualquier dirección que no esté en la lista queda prohibida.
- **Preguntas que te pueden hacer:**
  - *¿Por qué los permisos están en el servidor y no solo en la app?* Porque la app se puede saltar (por ejemplo, llamando a la API con Swagger o Postman). El servidor es el único sitio seguro.
  - *¿Qué pasa si añado un controlador nuevo y me olvido de los permisos?* Que da 403 a todo el mundo, nunca queda abierto.

## Lo siguiente (fase 2B)

Ajustar el modelo de datos al diseño de la memoria (tablas de `producto`, `material`, `maquina`, `registro_tiempo`, `consumo_material`, etc.), antes de hacer los DTOs del resto de entidades.
