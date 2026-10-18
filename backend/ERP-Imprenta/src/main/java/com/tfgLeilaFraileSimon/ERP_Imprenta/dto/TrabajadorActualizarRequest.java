package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// datos para modificar un trabajador (el id va en la url)
// la contraseña y el rol no son obligatorios, si no vienen se quedan los de antes
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
