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
    @SecurityRequirements 
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