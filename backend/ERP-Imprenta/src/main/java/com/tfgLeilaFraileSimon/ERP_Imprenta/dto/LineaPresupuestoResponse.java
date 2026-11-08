package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;

// lo que devuelve la api de una linea de presupuesto
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record LineaPresupuestoResponse(
        Integer id,
        Resumen presupuesto,
        Resumen producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal,
        BigDecimal total,
        BigDecimal costeMaterial,
        BigDecimal costeMaquina,
        BigDecimal costeManoObra,
        BigDecimal horasEstimadas,
        BigDecimal margen) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static LineaPresupuestoResponse desde(LineaPresupuesto l) {
        return new LineaPresupuestoResponse(
                l.getId(),
                Resumen.de(l.getPresupuesto()),
                Resumen.de(l.getProducto()),
                l.getCantidad(),
                l.getPrecioUnitario(),
                l.getSubtotal(),
                l.getTotal(),
                l.getCosteMaterial(),
                l.getCosteMaquina(),
                l.getCosteManoObra(),
                l.getHorasEstimadas(),
                l.getMargen());
    }
}
