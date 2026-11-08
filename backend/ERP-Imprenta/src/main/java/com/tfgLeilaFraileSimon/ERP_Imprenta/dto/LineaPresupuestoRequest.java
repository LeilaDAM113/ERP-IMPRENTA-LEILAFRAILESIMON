package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// datos para crear o modificar una linea de presupuesto
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
// el subtotal y el total no se mandan: los calcula CalculoService
public record LineaPresupuestoRequest(
        @NotNull(message = "El presupuesto es obligatorio")
        Integer idPresupuesto,
        @NotNull(message = "El producto es obligatorio")
        Integer idProducto,
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad tiene que ser mayor que 0")
        Integer cantidad,
        // si no hay costes, el precio por unidad se pone a mano
        BigDecimal precioUnitario,
        BigDecimal costeMaterial,
        BigDecimal costeMaquina,
        BigDecimal costeManoObra,
        BigDecimal horasEstimadas,
        BigDecimal margen) {
}
