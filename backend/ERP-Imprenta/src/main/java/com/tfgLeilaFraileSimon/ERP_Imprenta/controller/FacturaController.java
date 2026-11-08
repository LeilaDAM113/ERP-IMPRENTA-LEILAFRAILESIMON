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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.FacturaRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.FacturaResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.FacturaService;

import jakarta.validation.Valid;

// endpoints de Factura: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/factura")
public class FacturaController {
    private final FacturaService servicio;

    public FacturaController(FacturaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<FacturaResponse> listar() {
        return servicio.listar().stream().map(FacturaResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public FacturaResponse obtener(@PathVariable Integer id) {
        return FacturaResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public FacturaResponse anadir(@Valid @RequestBody FacturaRequest datos) {
        return FacturaResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public FacturaResponse actualizar(@PathVariable Integer id, @Valid @RequestBody FacturaRequest datos) {
        return FacturaResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
