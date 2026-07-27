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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.TrabajadorService;

/*
 * API REST de Trabajador: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/trabajador")
public class TrabajadorController {
    private final TrabajadorService servicio;

    public TrabajadorController(TrabajadorService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Trabajador> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Trabajador obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Trabajador anadir(@RequestBody Trabajador trabajador) {
        return servicio.guardar(trabajador);
    }

    @PutMapping("/{id}")
    public Trabajador actualizar(@PathVariable Integer id, @RequestBody Trabajador trabajador) {
        return servicio.actualizar(id, trabajador);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
