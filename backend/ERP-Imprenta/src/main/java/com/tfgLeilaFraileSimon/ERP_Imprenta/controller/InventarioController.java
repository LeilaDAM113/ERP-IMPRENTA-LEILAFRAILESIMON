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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.InventarioService;

/*
 * API REST de Inventario: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    private final InventarioService servicio;

    public InventarioController(InventarioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Inventario> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Inventario obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Inventario anadir(@RequestBody Inventario objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Inventario actualizar(@PathVariable Integer id, @RequestBody Inventario objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
