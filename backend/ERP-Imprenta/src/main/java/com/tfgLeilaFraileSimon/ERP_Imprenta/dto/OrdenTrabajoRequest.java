package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoOrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.PrioridadOrden;

import jakarta.validation.constraints.NotBlank;

// datos para crear o modificar una orden de trabajo
// no lleva id (va en la url al modificar) y las cosas relacionadas van solo con su id
public record OrdenTrabajoRequest(
        @NotBlank(message = "El numero es obligatorio")
        String numero,
        String titulo,
        String descripcion,
        Integer idPresupuesto,
        Integer idTrabajador,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDate fechaEntrega,
        PrioridadOrden prioridad,
        EstadoOrdenTrabajo estado) {
}
