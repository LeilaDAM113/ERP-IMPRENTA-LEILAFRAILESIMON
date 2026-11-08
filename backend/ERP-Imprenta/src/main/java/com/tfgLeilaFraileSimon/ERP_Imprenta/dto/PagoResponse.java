package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.MetodoPago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pago;

// lo que devuelve la api de un pago (el cobro de una factura)
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record PagoResponse(
        Integer id,
        Resumen factura,
        String descripcion,
        LocalDate fechaEmision,
        LocalDate fechaPago,
        MetodoPago metodoPago,
        BigDecimal importe,
        EstadoPago estado) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static PagoResponse desde(Pago p) {
        return new PagoResponse(
                p.getId(),
                Resumen.de(p.getFactura()),
                p.getDescripcion(),
                p.getFechaEmision(),
                p.getFechaPago(),
                p.getMetodoPago(),
                p.getImporte(),
                p.getEstado());
    }
}
