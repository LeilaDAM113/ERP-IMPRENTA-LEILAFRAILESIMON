package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
 * NOTA: no hay configuracion de CORS a proposito. La aplicacion se usa desde
 * el mismo origen (localhost) o desde Postman, que no es un navegador y no
 * aplica las restricciones CORS. Si en el futuro el frontend se sirve desde
 * OTRO origen (otro puerto o dominio), habria que anadir aqui una
 * configuracion de CORS con la lista de origenes permitidos.
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
    //  - anyRequest().authenticated() -> TODO endpoint requiere estar logueado
    //  - httpBasic -> se entra mandando email y contrasena (autenticacion basica)
    //  - requiresChannel (solo si exigirHttps=true) -> rechaza el trafico que
    //    no vaya por HTTPS; en local se deja apagado para poder probar.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated())
            .httpBasic(withDefaults -> {});

        if (exigirHttps) {
            http.requiresChannel(canal -> canal.anyRequest().requiresSecure());
        }

        return http.build();
    }
}
