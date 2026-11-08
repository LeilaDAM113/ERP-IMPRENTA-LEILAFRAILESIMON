package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Albaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoAlbaran;

// lo que devuelve la api de un albaran
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record AlbaranResponse(
        Integer id,
        String numero,
        Resumen cliente,
        Resumen ordenTrabajo,
        Resumen transportista,
        LocalDate fechaEmision,
        LocalDateTime fechaEntrega,
        String firma,
        String nombreReceptor,
        String observaciones,
        EstadoAlbaran estado) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static AlbaranResponse desde(Albaran a) {
        return new AlbaranResponse(
                a.getId(),
                a.getNumero(),
                Resumen.de(a.getCliente()),
                Resumen.de(a.getOrdenTrabajo()),
                Resumen.de(a.getTransportista()),
                a.getFechaEmision(),
                a.getFechaEntrega(),
                a.getFirma(),
                a.getNombreReceptor(),
                a.getObservaciones(),
                a.getEstado());
    }
}
