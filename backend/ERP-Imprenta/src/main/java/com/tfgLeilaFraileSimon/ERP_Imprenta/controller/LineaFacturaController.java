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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.LineaFacturaService;

/*
 * API REST de LineaFactura: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/linea-factura")
public class LineaFacturaController {
    private final LineaFacturaService servicio;

    public LineaFacturaController(LineaFacturaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<LineaFactura> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public LineaFactura obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public LineaFactura anadir(@RequestBody LineaFactura objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public LineaFactura actualizar(@PathVariable Integer id, @RequestBody LineaFactura objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
