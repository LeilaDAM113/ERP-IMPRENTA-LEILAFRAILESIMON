package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.MetodoPago;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// datos para crear o modificar un pago (el cobro de una factura)
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
public record PagoRequest(
        @NotNull(message = "La factura es obligatoria")
        Integer idFactura,
        String descripcion,
        LocalDate fechaEmision,
        LocalDate fechaPago,
        MetodoPago metodoPago,
        @NotNull(message = "El importe es obligatorio")
        @Positive(message = "El importe tiene que ser mayor que 0")
        BigDecimal importe,
        EstadoPago estado) {
}
