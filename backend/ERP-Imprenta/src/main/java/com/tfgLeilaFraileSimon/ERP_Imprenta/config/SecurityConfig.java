package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

/*
 * CONFIGURACION DE SEGURIDAD (las "reglas del portero").
 * Aqui decidimos:
 *   1. Como se cifran/comprueban las contrasenas -> con BCrypt (la "batidora"
 *      que deja la contrasena irreconocible e imposible de deshacer).
 *   2. Que hace falta para acceder a la aplicacion -> estar autenticado.
 *   3. Que la sesion no se guarde en el servidor (STATELESS), como corresponde
 *      a una API REST.
 *   4. Si se debe obligar a usar HTTPS (solo en produccion).
 *
 * NOTA: no hay configuracion de CORS a proposito. El frontend (HTML/CSS/JS,
 * carpeta "frontend" en la raiz del proyecto, ver spring.web.resources.static-locations
 * en application.properties) se sirve desde este MISMO backend, en el mismo
 * origen (localhost:8080), asi que el navegador nunca lo considera una
 * peticion "cruzada" y no aplican las restricciones CORS. Si en el futuro el
 * frontend se sirve desde OTRO origen (por ejemplo un servidor de desarrollo
 * en otro puerto), habria que anadir aqui una configuracion de CORS con la
 * lista de origenes permitidos.
 *
 * @EnableMethodSecurity activa las anotaciones @PreAuthorize que usamos en
 * los controladores para decir "esto solo lo puede hacer un ADMIN".
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // En local se deja en false para poder probar sin certificado; en
    // produccion se pone a true (application.properties) para forzar HTTPS.
    @Value("${app.security.require-https:false}")
    private boolean exigirHttps;

    // BCrypt: cifra las contrasenas al guardarlas y las compara al hacer login.
    // Spring usa este mismo "encoder" automaticamente para verificar el password.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Hace que Spring Security avise con un "evento" cada vez que alguien
    // intenta iniciar sesion, tanto si acierta como si falla. LoginAttemptListener
    // escucha esos eventos para contar los fallos y bloquear el email despues
    // de varios intentos seguidos (proteccion por fuerza bruta).
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publicador) {
        return new DefaultAuthenticationEventPublisher(publicador);
    }

    // Reglas de acceso:
    //  - csrf desactivado -> solo hace falta protegerse de CSRF cuando el
    //    navegador manda cookies de sesion solo; como esta API es STATELESS
    //    (no guarda sesion) y cada peticion lleva sus credenciales, no aplica.
    //  - sessionManagement STATELESS -> Spring no crea cookie de sesion tras
    //    el login; cada peticion se autentica sola con el header Authorization.
    //  - "/api/**" autenticado, TODO LO DEMAS libre -> el HTML/CSS/JS del
    //    frontend (index.html, css/, js/...) tiene que poder cargarse SIN
    //    login (si no, el navegador solo veria el cuadro feo de Basic Auth
    //    del sistema en vez de nuestra pantalla de login). Quien de verdad
    //    protege los datos es la API: cada llamada a /api/... que haga el
    //    JavaScript sigue exigiendo las credenciales igual que antes.
    //  - httpBasic -> se entra mandando email y contrasena (autenticacion basica)
    //  - requiresChannel (solo si exigirHttps=true) -> rechaza el trafico que
    //    no vaya por HTTPS; en local se deja apagado para poder probar.
            @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            AuthenticationEntryPoint respuestaNoAutorizada =
                    (request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                                "{\"message\":\"Email o contraseña incorrectos\"}"
                        );
                    };

            http.csrf(AbstractHttpConfigurer::disable).sessionManagement(session ->
                            session.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS
                            )
                        ).authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll()
                        ).exceptionHandling(exceptions ->
                            exceptions.authenticationEntryPoint(
                                    respuestaNoAutorizada
                            )
                    ).httpBasic(basic ->
                            basic.authenticationEntryPoint(
                                    respuestaNoAutorizada
                            )
                    );

            if (exigirHttps) {
                http.requiresChannel(canal ->
                        canal.anyRequest().requiresSecure()
                );
            }

            return http.build();
        }
        
        }
