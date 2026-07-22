package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

/*
 * CONTROLADOR DE PRUEBA PARA COMPROBAR EL LOGIN.
 * Expone la direccion GET /api/me. Como todos los endpoints requieren login,
 * solo se puede llamar estando autenticado.
 * @AuthenticationPrincipal -> Spring nos da automaticamente el usuario que
 * acaba de iniciar sesion, y respondemos con su nombre para confirmar que
 * el login ha funcionado. Es solo una prueba; luego iran los endpoints reales.
 */
@RestController
@RequestMapping("/api/me")
public class AuthController {

    @GetMapping
    public String me(@AuthenticationPrincipal Trabajador user) {
        if(user.getNombreCompleto()!=null){
        return "Hola, " + user.getNombreCompleto() + " (" + user.getEmail() + ")";
        }
        return "No existe la persona con ese email";
    }
}
