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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Albaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.AlbaranService;

/*
 * API REST de Albaran: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/albaran")
public class AlbaranController {
    private final AlbaranService servicio;

    public AlbaranController(AlbaranService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Albaran> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Albaran obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Albaran anadir(@RequestBody Albaran objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Albaran actualizar(@PathVariable Integer id, @RequestBody Albaran objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
