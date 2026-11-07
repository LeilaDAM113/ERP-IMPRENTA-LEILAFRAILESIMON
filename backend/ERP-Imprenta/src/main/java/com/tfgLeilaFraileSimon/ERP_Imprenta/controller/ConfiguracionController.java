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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Configuracion;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ConfiguracionService;

@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {
    private final ConfiguracionService servicio;

    public ConfiguracionController(ConfiguracionService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Configuracion> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Configuracion obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Configuracion anadir(@RequestBody Configuracion objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Configuracion actualizar(@PathVariable Integer id, @RequestBody Configuracion objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
