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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.OrdenTrabajadorService;

/*
 * API REST de OrdenTrabajador: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/orden-trabajador")
public class OrdenTrabajadorController {
    private final OrdenTrabajadorService servicio;

    public OrdenTrabajadorController(OrdenTrabajadorService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<OrdenTrabajador> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public OrdenTrabajador obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public OrdenTrabajador anadir(@RequestBody OrdenTrabajador objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public OrdenTrabajador actualizar(@PathVariable Integer id, @RequestBody OrdenTrabajador objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
