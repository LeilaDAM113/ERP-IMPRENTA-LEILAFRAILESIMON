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

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.RegistroTiempoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.RegistroTiempoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.RegistroTiempoService;

import jakarta.validation.Valid;

// endpoints de RegistroTiempo: listar, ver uno, crear, modificar y borrar
// recibe y devuelve DTOs
@RestController
@RequestMapping("/api/registro-tiempo")
public class RegistroTiempoController {
    private final RegistroTiempoService servicio;

    public RegistroTiempoController(RegistroTiempoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<RegistroTiempoResponse> listar() {
        return servicio.listar().stream().map(RegistroTiempoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public RegistroTiempoResponse obtener(@PathVariable Integer id) {
        return RegistroTiempoResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    public RegistroTiempoResponse anadir(@Valid @RequestBody RegistroTiempoRequest datos) {
        return RegistroTiempoResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public RegistroTiempoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody RegistroTiempoRequest datos) {
        return RegistroTiempoResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
