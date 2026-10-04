package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/*
 * Expone GET /api/me: "¿quien soy y con que permisos he entrado?".
 * Como todos los endpoints de /api/** requieren login, solo se puede llamar
 * estando autenticado. @AuthenticationPrincipal -> Spring nos da automaticamente
 * el Trabajador que acaba de iniciar sesion.
 *
 * Devuelve el propio Trabajador (igual que el resto de la API, sin DTOs) para
 * que el frontend pueda leer nombreCompleto/email/rol y decidir que mostrar
 * (por ejemplo, ocultar los botones de administracion si el rol no es ADMIN).
 * La contrasena nunca viaja porque el campo esta marcado WRITE_ONLY.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String postMethodName(@RequestBody String entity) {
        
        return entity;
    }
    
}
