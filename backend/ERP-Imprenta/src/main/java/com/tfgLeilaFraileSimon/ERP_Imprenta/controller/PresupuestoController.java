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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PresupuestoService;

/*
 * API REST de Presupuesto: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/presupuesto")
public class PresupuestoController {
    private final PresupuestoService servicio;

    public PresupuestoController(PresupuestoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Presupuesto> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Presupuesto obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Presupuesto anadir(@RequestBody Presupuesto objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Presupuesto actualizar(@PathVariable Integer id, @RequestBody Presupuesto objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
