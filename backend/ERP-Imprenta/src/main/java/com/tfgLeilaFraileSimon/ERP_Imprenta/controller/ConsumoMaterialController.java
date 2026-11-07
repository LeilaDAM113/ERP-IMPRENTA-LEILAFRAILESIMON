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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ConsumoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ConsumoMaterialService;


@RestController
@RequestMapping("/api/consumo-material")
public class ConsumoMaterialController {
    private final ConsumoMaterialService servicio;

    public ConsumoMaterialController(ConsumoMaterialService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ConsumoMaterial> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ConsumoMaterial obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ConsumoMaterial anadir(@RequestBody ConsumoMaterial objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public ConsumoMaterial actualizar(@PathVariable Integer id, @RequestBody ConsumoMaterial objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
