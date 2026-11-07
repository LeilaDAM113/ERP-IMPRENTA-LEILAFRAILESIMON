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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ProductoProceso;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ProductoProcesoService;

@RestController
@RequestMapping("/api/producto-proceso")
public class ProductoProcesoController {
    private final ProductoProcesoService servicio;

    public ProductoProcesoController(ProductoProcesoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ProductoProceso> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ProductoProceso obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ProductoProceso anadir(@RequestBody ProductoProceso objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public ProductoProceso actualizar(@PathVariable Integer id, @RequestBody ProductoProceso objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
