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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Empresa;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.EmpresaService;

/*
 * API REST de Empresa: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {
    private final EmpresaService servicio;

    public EmpresaController(EmpresaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Empresa> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Empresa obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Empresa anadir(@RequestBody Empresa objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public Empresa actualizar(@PathVariable Integer id, @RequestBody Empresa objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
