package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/*
 * Datos que manda la aplicacion al iniciar sesion (POST /auth/login).
 */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,
        @NotBlank(message = "La contrasena es obligatoria")
        String password) {
}