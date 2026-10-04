package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/*
 * ESCUCHA los eventos de login que publica Spring Security (gracias al bean
 * AuthenticationEventPublisher de SecurityConfig) y se los pasa a
 * LoginAttemptService para llevar la cuenta de fallos por email.
 *
 * No hace falta llamar a esta clase desde ningun sitio: Spring la detecta
 * sola por el @EventListener y la ejecuta cada vez que alguien intenta
 * iniciar sesion.
 */
@Component
public class LoginAttemptListener {
    private final LoginAttemptService loginAttemptService;

    public LoginAttemptListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    // Login fallido por contrasena incorrecta -> sumamos un fallo a ese email
    // qué es un eventlistener y como funciona
    @EventListener
    public void alFallarLogin(AuthenticationFailureBadCredentialsEvent evento) {
        String email = evento.getAuthentication().getName();
        loginAttemptService.registrarFallo(email);
    }

    // Login correcto -> reseteamos el contador de fallos de ese email
    @EventListener
    public void alAcertarLogin(AuthenticationSuccessEvent evento) {
        String email = evento.getAuthentication().getName();
        loginAttemptService.registrarExito(email);
    }
}
