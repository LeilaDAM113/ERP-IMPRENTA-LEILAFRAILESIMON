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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaAlbaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.LineaAlbaranService;

/*
 * API REST de LineaAlbaran: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/linea-albaran")
public class LineaAlbaranController {
    private final LineaAlbaranService servicio;

    public LineaAlbaranController(LineaAlbaranService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<LineaAlbaran> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public LineaAlbaran obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public LineaAlbaran anadir(@RequestBody LineaAlbaran objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public LineaAlbaran actualizar(@PathVariable Integer id, @RequestBody LineaAlbaran objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
