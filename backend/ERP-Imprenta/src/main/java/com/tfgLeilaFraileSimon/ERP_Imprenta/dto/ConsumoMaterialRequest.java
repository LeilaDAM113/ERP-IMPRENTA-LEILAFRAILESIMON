package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// datos para crear o modificar un consumo de material
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
public record ConsumoMaterialRequest(
        @NotNull(message = "La orden de trabajo es obligatoria")
        Integer idOrdenTrabajo,
        @NotNull(message = "El material es obligatorio")
        Integer idMaterial,
        Integer idTrabajador,
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad tiene que ser mayor que 0")
        BigDecimal cantidad,
        LocalDateTime fecha) {
}
