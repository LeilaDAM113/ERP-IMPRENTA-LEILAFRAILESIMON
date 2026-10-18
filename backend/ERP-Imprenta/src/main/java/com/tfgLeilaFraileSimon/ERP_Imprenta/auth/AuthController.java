package com.tfgLeilaFraileSimon.ERP_Imprenta.auth;
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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.TrabajadorService;

import jakarta.validation.Valid;

// login (POST /auth/login) y quien soy (GET /api/me)
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
        // si ha fallado 5 veces esta bloqueado -> 429
        if (loginAttemptService.estaBloqueado(datos.email())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Cuenta bloqueada por demasiados intentos fallidos. Intentalo en unos minutos.");
        }
        try {
            // spring mira si el email y la contraseña estan bien
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(datos.email(), datos.password()));
            // si esta bien le creo el token
            Trabajador trabajador = (Trabajador) auth.getPrincipal();
            return new LoginResponse(tokenService.generarToken(trabajador), tokenService.segundosValidez(),
                    TrabajadorResponse.desde(trabajador));
        } catch (AuthenticationException e) {
            // mismo mensaje falle el email o la contraseña, asi no doy pistas
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");
        }
    }

    @GetMapping("/api/me")
    public TrabajadorResponse yo(@AuthenticationPrincipal Jwt jwt) {
        // dentro del token va el email 
        return TrabajadorResponse.desde(trabajadorService.obtenerPorEmail(jwt.getSubject()));
    }
}