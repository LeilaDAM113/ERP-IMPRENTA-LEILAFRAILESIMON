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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LineaPresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LineaPresupuestoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.LineaPresupuestoService;

import jakarta.validation.Valid;

// endpoints de LineaPresupuesto: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/linea-presupuesto")
public class LineaPresupuestoController {
    private final LineaPresupuestoService servicio;

    public LineaPresupuestoController(LineaPresupuestoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<LineaPresupuestoResponse> listar() {
        return servicio.listar().stream().map(LineaPresupuestoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public LineaPresupuestoResponse obtener(@PathVariable Integer id) {
        return LineaPresupuestoResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public LineaPresupuestoResponse anadir(@Valid @RequestBody LineaPresupuestoRequest datos) {
        return LineaPresupuestoResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public LineaPresupuestoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody LineaPresupuestoRequest datos) {
        return LineaPresupuestoResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
