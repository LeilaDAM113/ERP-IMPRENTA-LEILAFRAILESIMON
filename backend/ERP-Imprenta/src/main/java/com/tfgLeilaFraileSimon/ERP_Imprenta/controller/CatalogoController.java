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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.CatalogoService;

/*
 * API REST de Catalogo: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {
    private final CatalogoService servicio;

    public CatalogoController(CatalogoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Catalogo> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Catalogo obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Catalogo anadir(@RequestBody Catalogo objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Catalogo actualizar(@PathVariable Integer id, @RequestBody Catalogo objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
