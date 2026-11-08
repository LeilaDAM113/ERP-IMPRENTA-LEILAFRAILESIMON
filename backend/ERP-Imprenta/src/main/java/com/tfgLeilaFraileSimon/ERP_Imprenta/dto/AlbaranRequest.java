package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoAlbaran;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// datos para crear o modificar un albaran
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
public record AlbaranRequest(
        @NotBlank(message = "El numero es obligatorio")
        String numero,
        @NotNull(message = "El cliente es obligatorio")
        Integer idCliente,
        Integer idOrdenTrabajo,
        Integer idTransportista,
        LocalDate fechaEmision,
        LocalDateTime fechaEntrega,
        String firma,
        String nombreReceptor,
        String observaciones,
        EstadoAlbaran estado) {
}
