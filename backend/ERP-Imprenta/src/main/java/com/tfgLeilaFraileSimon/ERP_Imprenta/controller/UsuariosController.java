package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api")
public class UsuariosController {
    
    public String postMethodName(@RequestBody String entity , @AuthenticationPrincipal Jwt auth) {
        
        return entity;
    }
}
