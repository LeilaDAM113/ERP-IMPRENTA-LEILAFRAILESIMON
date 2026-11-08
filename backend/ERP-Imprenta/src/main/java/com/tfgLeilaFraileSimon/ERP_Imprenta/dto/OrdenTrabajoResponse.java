package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoOrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.PrioridadOrden;

// lo que devuelve la api de una orden de trabajo
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record OrdenTrabajoResponse(
        Integer id,
        String numero,
        String titulo,
        String descripcion,
        Resumen presupuesto,
        Resumen trabajador,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDate fechaEntrega,
        PrioridadOrden prioridad,
        EstadoOrdenTrabajo estado) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static OrdenTrabajoResponse desde(OrdenTrabajo o) {
        return new OrdenTrabajoResponse(
                o.getId(),
                o.getNumero(),
                o.getTitulo(),
                o.getDescripcion(),
                Resumen.de(o.getPresupuesto()),
                Resumen.de(o.getTrabajador()),
                o.getFechaInicio(),
                o.getFechaFin(),
                o.getFechaEntrega(),
                o.getPrioridad(),
                o.getEstado());
    }
}
