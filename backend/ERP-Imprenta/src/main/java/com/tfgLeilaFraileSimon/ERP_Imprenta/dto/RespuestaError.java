package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;
import java.time.LocalDateTime;
import java.util.Map;

// Asi es como devuelve la API todos los errores, siempre igual,
// para que las apps puedan enseñar el mensaje al usuario.
// errores = el fallo de cada campo del formulario (si lo hay)
public record RespuestaError(int estado, String mensaje, Map<String, String> errores, String ruta,
        LocalDateTime fecha) {
}