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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.FacturaService;

/*
 * API REST de Factura: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/factura")
public class FacturaController {
    private final FacturaService servicio;

    public FacturaController(FacturaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Factura> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Factura obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Factura anadir(@RequestBody Factura objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Factura actualizar(@PathVariable Integer id, @RequestBody Factura objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
