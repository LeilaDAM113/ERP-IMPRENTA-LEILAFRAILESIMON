package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// datos para crear o modificar un presupuesto
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
// tampoco lleva el estado: se cambia con los botones enviar, aceptar y rechazar
// ni el subtotal ni el importe: salen de sumar las lineas (CalculoService)
public record PresupuestoRequest(
        @NotBlank(message = "El numero es obligatorio")
        String numero,
        @NotNull(message = "El cliente es obligatorio")
        Integer idCliente,
        Integer idComercial,
        LocalDate fechaEmision,
        LocalDate fechaValidez,
        String descripcion,
        BigDecimal porcentajeIva) {
}
