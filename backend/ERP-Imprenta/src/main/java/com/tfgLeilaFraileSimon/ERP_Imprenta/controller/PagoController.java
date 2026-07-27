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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PagoService;

/*
 * API REST de Pago: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/pago")
public class PagoController {
    private final PagoService servicio;

    public PagoController(PagoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Pago> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Pago obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Pago anadir(@RequestBody Pago objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Pago actualizar(@PathVariable Integer id, @RequestBody Pago objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
