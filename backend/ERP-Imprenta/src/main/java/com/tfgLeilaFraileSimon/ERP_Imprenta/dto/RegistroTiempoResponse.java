package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.time.LocalDateTime;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RegistroTiempo;

// lo que devuelve la api de un registro de tiempo
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record RegistroTiempoResponse(
        Integer id,
        Resumen ordenTrabajo,
        Resumen trabajador,
        Resumen maquina,
        LocalDateTime inicio,
        LocalDateTime fin,
        Integer minutos,
        String observaciones) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static RegistroTiempoResponse desde(RegistroTiempo r) {
        return new RegistroTiempoResponse(
                r.getId(),
                Resumen.de(r.getOrdenTrabajo()),
                Resumen.de(r.getTrabajador()),
                Resumen.de(r.getMaquina()),
                r.getInicio(),
                r.getFin(),
                r.getMinutos(),
                r.getObservaciones());
    }
}
