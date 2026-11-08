package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ConsumoMaterial;

// lo que devuelve la api de un consumo de material
// las cosas relacionadas salen resumidas: solo su id y su nombre
public record ConsumoMaterialResponse(
        Integer id,
        Resumen ordenTrabajo,
        Resumen material,
        Resumen trabajador,
        BigDecimal cantidad,
        LocalDateTime fecha) {

    // paso de la entidad (la tabla) al DTO (lo que ve la app)
    public static ConsumoMaterialResponse desde(ConsumoMaterial c) {
        return new ConsumoMaterialResponse(
                c.getId(),
                Resumen.de(c.getOrdenTrabajo()),
                Resumen.de(c.getMaterial()),
                Resumen.de(c.getTrabajador()),
                c.getCantidad(),
                c.getFecha());
    }
}
