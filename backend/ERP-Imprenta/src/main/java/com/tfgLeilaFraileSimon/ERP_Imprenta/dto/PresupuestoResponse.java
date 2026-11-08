package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;

// lo que devuelve la api de un presupuesto
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record PresupuestoResponse(
        Integer id,
        String numero,
        Resumen cliente,
        Resumen comercial,
        LocalDate fechaEmision,
        LocalDate fechaEnvio,
        LocalDate fechaValidez,
        String descripcion,
        BigDecimal subtotal,
        BigDecimal importe,
        BigDecimal porcentajeIva,
        EstadoPresupuesto estado) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static PresupuestoResponse desde(Presupuesto p) {
        return new PresupuestoResponse(
                p.getId(),
                p.getNumero(),
                Resumen.de(p.getCliente()),
                Resumen.de(p.getComercial()),
                p.getFechaEmision(),
                p.getFechaEnvio(),
                p.getFechaValidez(),
                p.getDescripcion(),
                p.getSubtotal(),
                p.getImporte(),
                p.getPorcentajeIva(),
                p.getEstado());
    }
}
