package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Datos que se aceptan al ACTUALIZAR un trabajador (PUT /api/trabajador/{id}).
 *
 * Tampoco lleva id: el trabajador a modificar lo indica la URL, no el JSON.
 *
 * Diferencia con TrabajadorCrearRequest: aqui la contrasena es OPCIONAL. Si
 * no viene (o viene vacia) se conserva la que ya tenia; si viene, tiene que
 * tener al menos 8 caracteres (@Size deja pasar null, por eso sirve aqui).
 * Lo mismo con el rol: si no viene, se conserva el actual.
 */
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
