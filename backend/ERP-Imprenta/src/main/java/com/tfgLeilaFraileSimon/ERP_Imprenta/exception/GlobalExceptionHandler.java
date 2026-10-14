package com.tfgLeilaFraileSimon.ERP_Imprenta.exception;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.RespuestaError;
import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

// Aqui llegan todos los errores de la API (se personalizan para saber exactamente por qué ha fallado)
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Monto la respuesta de error siempre con el mismo formato
    private ResponseEntity<RespuestaError> respuesta(HttpStatusCode estado, String mensaje,
            Map<String, String> errores, HttpServletRequest peticion) {
        RespuestaError cuerpo = new RespuestaError(estado.value(), mensaje, errores,
                peticion.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.status(estado).body(cuerpo);
    }

    // Piden un id que no existe -> 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RespuestaError> noEncontrado(NoSuchElementException e, HttpServletRequest peticion) {
        return respuesta(HttpStatus.NOT_FOUND, "No existe ningun registro con ese id", null, peticion);
    }

    // Algun campo no cumple las reglas (@NotBlank, @Email...) -> 400
    // y digo que campo falla para que la app lo pueda marcar en rojo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> datosNoValidos(MethodArgumentNotValidException e,
            HttpServletRequest peticion) {
        Map<String, String> errores = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "Hay datos que no son validos", errores, peticion);
    }

    // El JSON viene mal escrito o con un valor raro -> 400
    // (por ejemplo "rol": "Operario" en vez de "OPERARIO")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError> jsonNoValido(HttpMessageNotReadableException e,
            HttpServletRequest peticion) {
        if (e.getMostSpecificCause() instanceof InvalidFormatException formato) {
            String campo = formato.getPath().isEmpty() ? "dato"
                    : formato.getPath().get(formato.getPath().size() - 1).getPropertyName();
            String mensaje = "El valor '" + formato.getValue() + "' no es valido para " + campo;
            // si es un enum (rol, estado...) le digo que valores puede poner
            if (formato.getTargetType() != null && formato.getTargetType().isEnum()) {
                mensaje += ". Valores permitidos: " + Arrays.toString(formato.getTargetType().getEnumConstants());
            }
            return respuesta(HttpStatus.BAD_REQUEST, mensaje, Map.of(campo, mensaje), peticion);
        }
        return respuesta(HttpStatus.BAD_REQUEST,
                "El JSON no es valido (revisa comillas, comas, numeros y fechas AAAA-MM-DD)", null, peticion);
    }

    // En la URL ponen algo que no es un numero, por ejemplo /api/cliente/abc -> 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespuestaError> parametroNoValido(MethodArgumentTypeMismatchException e,
            HttpServletRequest peticion) {
        return respuesta(HttpStatus.BAD_REQUEST, "El valor '" + e.getValue() + "' no es valido para " + e.getName(),
                null, peticion);
    }

    // La base de datos no deja guardar o borrar -> 409
    // pasa con un email repetido o al borrar algo que se esta usando
    // (por ejemplo un cliente que ya tiene presupuestos)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaError> conflicto(DataIntegrityViolationException e,
            HttpServletRequest peticion) {
        return respuesta(HttpStatus.CONFLICT,
                "No se puede guardar o borrar: hay un dato repetido o el registro lo esta usando otro", null,
                peticion);
    }

    // El rol no tiene permiso para hacer eso -> 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RespuestaError> sinPermiso(AccessDeniedException e, HttpServletRequest peticion) {
        return respuesta(HttpStatus.FORBIDDEN, "No tienes permiso para hacer esta operacion", null, peticion);
    }

    // Cualquier otro error.
    // Si ya trae su codigo (por ejemplo el 401 y el 429 del login, o un 405)
    // respeto ese codigo. Si no, es un fallo que no esperaba -> 500,
    // lo apunto en la consola y al usuario no le enseño detalles internos.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> otroError(Exception e, HttpServletRequest peticion) {
        if (e instanceof ErrorResponse conCodigo) {
            return respuesta(conCodigo.getStatusCode(), conCodigo.getBody().getDetail(), null, peticion);
        }
        log.error("Error inesperado en {}", peticion.getRequestURI(), e);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado en el servidor", null,
                peticion);
    }
}