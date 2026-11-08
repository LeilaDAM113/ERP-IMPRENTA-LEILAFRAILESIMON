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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.AlbaranRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.AlbaranResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.AlbaranService;

import jakarta.validation.Valid;

// endpoints de Albaran: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/albaran")
public class AlbaranController {
    private final AlbaranService servicio;

    public AlbaranController(AlbaranService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<AlbaranResponse> listar() {
        return servicio.listar().stream().map(AlbaranResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public AlbaranResponse obtener(@PathVariable Integer id) {
        return AlbaranResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public AlbaranResponse anadir(@Valid @RequestBody AlbaranRequest datos) {
        return AlbaranResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public AlbaranResponse actualizar(@PathVariable Integer id, @Valid @RequestBody AlbaranRequest datos) {
        return AlbaranResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
