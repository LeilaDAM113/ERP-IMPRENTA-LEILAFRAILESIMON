package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.security.TrabajadorUserDetails;

@RestController
public class AuthController {

    @GetMapping("/api/me")
    public String me(@AuthenticationPrincipal TrabajadorUserDetails user) {
        return "Hola, " + user.getTrabajador().getNombreCompleto() + " (" + user.getUsername() + ")";
    }
}
