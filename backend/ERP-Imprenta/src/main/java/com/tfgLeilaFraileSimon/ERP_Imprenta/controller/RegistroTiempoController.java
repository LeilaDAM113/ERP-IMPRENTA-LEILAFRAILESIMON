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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RegistroTiempo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.RegistroTiempoService;

@RestController
@RequestMapping("/api/registro-tiempo")
public class RegistroTiempoController {
    private final RegistroTiempoService servicio;

    public RegistroTiempoController(RegistroTiempoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<RegistroTiempo> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public RegistroTiempo obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public RegistroTiempo anadir(@RequestBody RegistroTiempo objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public RegistroTiempo actualizar(@PathVariable Integer id, @RequestBody RegistroTiempo objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
