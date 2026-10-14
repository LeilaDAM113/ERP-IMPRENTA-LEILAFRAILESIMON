package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Datos para modificar un trabajador (PUT /api/trabajador/{id}).
// El id va en la URL, no aqui.
// Aqui la contraseña y el rol son opcionales: si no vienen, se quedan los que tenia.
// Si viene contraseña, tiene que tener al menos 8 caracteres.
public record TrabajadorActualizarRequest(
        String nombreCompleto,
        String dni,
        String telefono,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
        String password,
        Integer idPuesto,
        BigDecimal salario,
        LocalDate fechaAlta,
        Boolean activo,
        RolTrabajador rol) {
}
