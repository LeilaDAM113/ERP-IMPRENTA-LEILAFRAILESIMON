package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.MetodoPago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// datos para crear o modificar una factura
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
// la cuota de IVA y el total no se mandan: los calcula CalculoService
public record FacturaRequest(
        @NotBlank(message = "El numero es obligatorio")
        String numero,
        @NotNull(message = "El cliente es obligatorio")
        Integer idCliente,
        Integer idOrdenTrabajo,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        LocalDate fechaPago,
        MetodoPago metodoPago,
        EstadoFactura estado,
        @NotNull(message = "La base imponible es obligatoria")
        BigDecimal baseImponible,
        BigDecimal porcentajeIva) {
}
