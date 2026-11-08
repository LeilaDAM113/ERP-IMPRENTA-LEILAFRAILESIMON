package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
