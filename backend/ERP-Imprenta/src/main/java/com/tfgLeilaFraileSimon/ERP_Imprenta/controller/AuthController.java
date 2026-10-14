package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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

// Login y "quien soy":
// - POST /auth/login: compruebo el email y la contraseña y devuelvo un token
// - GET /api/me: devuelvo los datos del que ha iniciado sesion
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
    @SecurityRequirements 
    public LoginResponse login(@Valid @RequestBody LoginRequest datos) {
        // si ha fallado 5 veces seguidas, esta bloqueado (429)
        if (loginAttemptService.estaBloqueado(datos.email())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Cuenta bloqueada por demasiados intentos fallidos. Intentalo en unos minutos.");
        }
        try {
            // Spring comprueba que el email y la contraseña estan bien
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(datos.email(), datos.password()));
            // si esta bien, le creo su token
            Trabajador trabajador = (Trabajador) auth.getPrincipal();
            return new LoginResponse(tokenService.generarToken(trabajador), tokenService.segundosValidez(),
                    TrabajadorResponse.desde(trabajador));
        } catch (AuthenticationException e) {
            // pongo el mismo mensaje si falla el email o la contraseña, para no dar pistas
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");
        }
    }

    @GetMapping("/api/me")
    public TrabajadorResponse yo(@AuthenticationPrincipal Jwt jwt) {
        // dentro del token va el email (lo meti al hacer login)
        return TrabajadorResponse.desde(trabajadorService.obtenerPorEmail(jwt.getSubject()));
    }
}