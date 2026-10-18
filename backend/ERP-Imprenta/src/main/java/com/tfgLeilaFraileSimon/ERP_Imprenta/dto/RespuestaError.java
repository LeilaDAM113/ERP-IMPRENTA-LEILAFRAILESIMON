package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;
import java.time.LocalDateTime;
import java.util.Map;

// asi devuelve la api todos los errores
// en errores va lo que falla de cada campo (si hay)
public record RespuestaError(int estado, String mensaje, Map<String, String> errores, String ruta,
        LocalDateTime fecha) {
}