package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.OrdenTrabajoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PresupuestoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PresupuestoService;

import jakarta.validation.Valid;

// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/presupuesto")
public class PresupuestoController {
    private final PresupuestoService servicio;

    public PresupuestoController(PresupuestoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<PresupuestoResponse> listar() {
        return servicio.listar().stream().map(PresupuestoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public PresupuestoResponse obtener(@PathVariable Integer id) {
        return PresupuestoResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public PresupuestoResponse anadir(@Valid @RequestBody PresupuestoRequest datos) {
        return PresupuestoResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public PresupuestoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody PresupuestoRequest datos) {
        return PresupuestoResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }

    // ---- ACCIONES (los botones del presupuesto) ----

    // BORRADOR -> ENVIADO
    @PostMapping("/{id}/enviar")
    public PresupuestoResponse enviar(@PathVariable Integer id) {
        return PresupuestoResponse.desde(servicio.enviar(id));
    }

    // ENVIADO -> RECHAZADO
    @PostMapping("/{id}/rechazar")
    public PresupuestoResponse rechazar(@PathVariable Integer id) {
        return PresupuestoResponse.desde(servicio.rechazar(id));
    }

    // ENVIADO -> ACEPTADO, y devuelvo la orden de trabajo que se ha creado
    @PostMapping("/{id}/aceptar")
    public OrdenTrabajoResponse aceptar(@PathVariable Integer id) {
        return OrdenTrabajoResponse.desde(servicio.aceptar(id));
    }

    // el presupuesto en PDF (para verlo, imprimirlo o mandarlo al cliente)
    // "inline" = que el navegador lo abra en vez de descargarlo
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Integer id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=presupuesto-" + id + ".pdf")
                .body(servicio.pdf(id));
    }
}
