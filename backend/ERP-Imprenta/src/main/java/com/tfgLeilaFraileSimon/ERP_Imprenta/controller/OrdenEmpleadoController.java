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

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenEmpleado;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.OrdenEmpleadoService;

/*
 * API REST de OrdenEmpleado: listar, obtener, anadir, actualizar y eliminar.
 */
@RestController
@RequestMapping("/api/orden-empleado")
public class OrdenEmpleadoController {
    private final OrdenEmpleadoService servicio;

    public OrdenEmpleadoController(OrdenEmpleadoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<OrdenEmpleado> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public OrdenEmpleado obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public OrdenEmpleado anadir(@RequestBody OrdenEmpleado objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public OrdenEmpleado actualizar(@PathVariable Integer id, @RequestBody OrdenEmpleado objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
