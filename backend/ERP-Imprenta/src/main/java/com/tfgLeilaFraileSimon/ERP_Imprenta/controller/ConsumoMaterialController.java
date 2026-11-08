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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.ConsumoMaterialRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.ConsumoMaterialResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ConsumoMaterialService;

import jakarta.validation.Valid;

// endpoints de ConsumoMaterial: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/consumo-material")
public class ConsumoMaterialController {
    private final ConsumoMaterialService servicio;

    public ConsumoMaterialController(ConsumoMaterialService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ConsumoMaterialResponse> listar() {
        return servicio.listar().stream().map(ConsumoMaterialResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public ConsumoMaterialResponse obtener(@PathVariable Integer id) {
        return ConsumoMaterialResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public ConsumoMaterialResponse anadir(@Valid @RequestBody ConsumoMaterialRequest datos) {
        return ConsumoMaterialResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public ConsumoMaterialResponse actualizar(@PathVariable Integer id, @Valid @RequestBody ConsumoMaterialRequest datos) {
        return ConsumoMaterialResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
