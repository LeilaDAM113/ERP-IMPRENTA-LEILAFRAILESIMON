package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Datos que se aceptan al CREAR un trabajador (POST /api/trabajador).
 *
 * No lleva id: lo genera la base de datos (autoincremental). Si el cliente lo
 * mandara en el JSON, Jackson lo ignora porque aqui no existe ese campo, asi
 * que no hay forma de "colar" un id y pisar a otro trabajador.
 *
 * Al crear, la contrasena es OBLIGATORIA (@NotBlank): no hay ninguna anterior
 * que conservar. El puesto se recibe solo por su id (idPuesto), no como objeto.
 */
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
        LocalDate fechaAlta,
        Boolean activo,
        RolTrabajador rol) {
}
