package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

// Esta clase "escucha" los logins que hace Spring.
// Cada vez que alguien inicia sesion (bien o mal) se ejecuta sola
// y se lo cuenta a LoginAttemptService para llevar la cuenta de fallos.
@Component
public class LoginAttemptListener {
    private final LoginAttemptService loginAttemptService;

    public LoginAttemptListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    // login fallido -> sumo un fallo a ese email
    // qué es un eventlistener y como funciona
    @EventListener
    public void alFallarLogin(AuthenticationFailureBadCredentialsEvent evento) {
        String email = evento.getAuthentication().getName();
        loginAttemptService.registrarFallo(email);
    }

    // login correcto -> pongo sus fallos a 0
    @EventListener
    public void alAcertarLogin(AuthenticationSuccessEvent evento) {
        String email = evento.getAuthentication().getName();
        loginAttemptService.registrarExito(email);
    }
}
