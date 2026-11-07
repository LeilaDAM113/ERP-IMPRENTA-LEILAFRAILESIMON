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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ProductoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ProductoMaterialService;

@RestController 
@RequestMapping("/api/producto-material")
public class ProductoMaterialController {
    private final ProductoMaterialService servicio;

    public ProductoMaterialController(ProductoMaterialService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ProductoMaterial> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ProductoMaterial obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ProductoMaterial anadir(@RequestBody ProductoMaterial objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public ProductoMaterial actualizar(@PathVariable Integer id, @RequestBody ProductoMaterial objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
