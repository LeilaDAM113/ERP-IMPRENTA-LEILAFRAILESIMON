package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.MetodoPago;

// lo que devuelve la api de una factura
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record FacturaResponse(
        Integer id,
        String numero,
        Resumen cliente,
        Resumen ordenTrabajo,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        LocalDate fechaPago,
        MetodoPago metodoPago,
        EstadoFactura estado,
        BigDecimal baseImponible,
        BigDecimal porcentajeIva,
        BigDecimal cuotaIva,
        BigDecimal total) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static FacturaResponse desde(Factura f) {
        return new FacturaResponse(
                f.getId(),
                f.getNumero(),
                Resumen.de(f.getCliente()),
                Resumen.de(f.getOrdenTrabajo()),
                f.getFechaEmision(),
                f.getFechaVencimiento(),
                f.getFechaPago(),
                f.getMetodoPago(),
                f.getEstado(),
                f.getBaseImponible(),
                f.getPorcentajeIva(),
                f.getCuotaIva(),
                f.getTotal());
    }
}
