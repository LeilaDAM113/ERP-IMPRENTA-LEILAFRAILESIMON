package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// datos para crear un trabajador
// no lleva id porque lo pone la base de datos
// la contraseña es obligatoria y del puesto solo mando el id
public record TrabajadorCrearRequest(
        String nombreCompleto,
        String dni,
        String telefono,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,
        @NotBlank(message = "La contrasena es obligatoria al crear un trabajador")
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
        String password,
        Integer idPuesto,
        BigDecimal salario,
        BigDecimal costeHora,
        LocalDate fechaAlta,
        Boolean activo,
        RolTrabajador rol) {
}
