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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.LineaPresupuestoService;

/*
 * API REST de LineaPresupuesto: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/linea-presupuesto")
public class LineaPresupuestoController {
    private final LineaPresupuestoService servicio;

    public LineaPresupuestoController(LineaPresupuestoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<LineaPresupuesto> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public LineaPresupuesto obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public LineaPresupuesto anadir(@RequestBody LineaPresupuesto objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public LineaPresupuesto actualizar(@PathVariable Integer id, @RequestBody LineaPresupuesto objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
