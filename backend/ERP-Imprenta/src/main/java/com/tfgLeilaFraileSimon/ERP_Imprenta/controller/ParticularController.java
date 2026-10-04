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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Particular;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ParticularService;

/*
 * API REST de Particular: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/particular")
public class ParticularController {
    private final ParticularService servicio;

    public ParticularController(ParticularService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Particular> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Particular obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Particular anadir(@RequestBody Particular objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Particular actualizar(@PathVariable Integer id, @RequestBody Particular objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
