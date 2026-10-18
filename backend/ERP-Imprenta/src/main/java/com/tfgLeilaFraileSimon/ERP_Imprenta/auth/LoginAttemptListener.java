package com.tfgLeilaFraileSimon.ERP_Imprenta.auth;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

// escucha los logins y le dice a LoginAttemptService si han salido bien o mal
@Component
public class LoginAttemptListener {
    private final LoginAttemptService loginAttemptService;

    public LoginAttemptListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    // login mal -> un fallo mas
    // qué es un eventlistener y como funciona
    @EventListener
    public void alFallarLogin(AuthenticationFailureBadCredentialsEvent evento) {
        String email = evento.getAuthentication().getName();
        loginAttemptService.registrarFallo(email);
    }

    // login bien -> fallos a 0
    @EventListener
    public void alAcertarLogin(AuthenticationSuccessEvent evento) {
        String email = evento.getAuthentication().getName();
        loginAttemptService.registrarExito(email);
    }
}
