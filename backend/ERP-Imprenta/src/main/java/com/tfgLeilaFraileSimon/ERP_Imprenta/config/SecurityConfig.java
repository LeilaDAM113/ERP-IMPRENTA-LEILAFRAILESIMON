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