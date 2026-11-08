package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

// datos para crear o modificar un registro de tiempo
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
public record RegistroTiempoRequest(
        @NotNull(message = "La orden de trabajo es obligatoria")
        Integer idOrdenTrabajo,
        @NotNull(message = "El trabajador es obligatorio")
        Integer idTrabajador,
        Integer idMaquina,
        @NotNull(message = "La hora de inicio es obligatoria")
        LocalDateTime inicio,
        LocalDateTime fin,
        Integer minutos,
        String observaciones) {
}
