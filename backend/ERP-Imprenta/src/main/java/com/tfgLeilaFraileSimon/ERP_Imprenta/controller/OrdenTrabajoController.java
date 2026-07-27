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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.OrdenTrabajoService;

/*
 * API REST de OrdenTrabajo: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/orden-trabajo")
public class OrdenTrabajoController {
    private final OrdenTrabajoService servicio;

    public OrdenTrabajoController(OrdenTrabajoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<OrdenTrabajo> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public OrdenTrabajo obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public OrdenTrabajo anadir(@RequestBody OrdenTrabajo objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public OrdenTrabajo actualizar(@PathVariable Integer id, @RequestBody OrdenTrabajo objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
