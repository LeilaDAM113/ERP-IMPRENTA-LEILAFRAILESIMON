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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.OrdenTrabajoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.OrdenTrabajoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.OrdenTrabajoService;

import jakarta.validation.Valid;

// endpoints de OrdenTrabajo: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/orden-trabajo")
public class OrdenTrabajoController {
    private final OrdenTrabajoService servicio;

    public OrdenTrabajoController(OrdenTrabajoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<OrdenTrabajoResponse> listar() {
        return servicio.listar().stream().map(OrdenTrabajoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public OrdenTrabajoResponse obtener(@PathVariable Integer id) {
        return OrdenTrabajoResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public OrdenTrabajoResponse anadir(@Valid @RequestBody OrdenTrabajoRequest datos) {
        return OrdenTrabajoResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public OrdenTrabajoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody OrdenTrabajoRequest datos) {
        return OrdenTrabajoResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
