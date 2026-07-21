package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * CONFIGURACION DE SEGURIDAD (las "reglas del portero").
 * Aqui decidimos DOS cosas:
 *   1. Como se cifran/comprueban las contrasenas -> con BCrypt (la "batidora"
 *      que deja la contrasena irreconocible e imposible de deshacer).
 *   2. Que hace falta para acceder a la aplicacion.
 */
@Configuration
public class SecurityConfig {

    // BCrypt: cifra las contrasenas al guardarlas y las compara al hacer login.
    // Spring usa este mismo "encoder" automaticamente para verificar el password.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Reglas de acceso:
    //  - anyRequest().authenticated() -> TODO endpoint requiere estar logueado
    //  - httpBasic -> se entra mandando email y contrasena (autenticacion basica)
    //  - csrf desactivado -> tipico en APIs REST (no usamos formularios de navegador)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated())
            .httpBasic(withDefaults -> {});
        return http.build();
    }
}
