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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ContactoEmpresa;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ContactoEmpresaService;

/*
 * API REST de ContactoEmpresa: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/contacto-empresa")
public class ContactoEmpresaController {
    private final ContactoEmpresaService servicio;

    public ContactoEmpresaController(ContactoEmpresaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ContactoEmpresa> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ContactoEmpresa obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ContactoEmpresa anadir(@RequestBody ContactoEmpresa objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public ContactoEmpresa actualizar(@PathVariable Integer id, @RequestBody ContactoEmpresa objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
