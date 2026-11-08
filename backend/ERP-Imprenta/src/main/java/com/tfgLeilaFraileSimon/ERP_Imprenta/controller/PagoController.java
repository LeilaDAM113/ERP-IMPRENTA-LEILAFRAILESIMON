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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PagoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PagoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PagoService;

import jakarta.validation.Valid;

// endpoints de Pago: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/pago")
public class PagoController {
    private final PagoService servicio;

    public PagoController(PagoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<PagoResponse> listar() {
        return servicio.listar().stream().map(PagoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public PagoResponse obtener(@PathVariable Integer id) {
        return PagoResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public PagoResponse anadir(@Valid @RequestBody PagoRequest datos) {
        return PagoResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public PagoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody PagoRequest datos) {
        return PagoResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
