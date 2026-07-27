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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PuestoService;

/*
 * API REST de Puesto: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/puesto")
public class PuestoController {
    private final PuestoService servicio;

    public PuestoController(PuestoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Puesto> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Puesto obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Puesto anadir(@RequestBody Puesto objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Puesto actualizar(@PathVariable Integer id, @RequestBody Puesto objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
