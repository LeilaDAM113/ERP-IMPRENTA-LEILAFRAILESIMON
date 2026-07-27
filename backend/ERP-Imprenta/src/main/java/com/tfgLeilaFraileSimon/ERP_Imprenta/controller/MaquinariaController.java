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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Maquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.MaquinariaService;

/*
 * API REST de Maquinaria: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/maquinaria")
public class MaquinariaController {
    private final MaquinariaService servicio;

    public MaquinariaController(MaquinariaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Maquinaria> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Maquinaria obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Maquinaria anadir(@RequestBody Maquinaria objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Maquinaria actualizar(@PathVariable Integer id, @RequestBody Maquinaria objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
