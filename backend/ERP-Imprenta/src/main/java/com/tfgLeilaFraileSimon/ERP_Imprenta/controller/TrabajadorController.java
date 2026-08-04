package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.TrabajadorService;

import jakarta.validation.Valid;

/*
 * API REST de Trabajador: listar, obtener, anadir, actualizar y eliminar.
 *
 * Dar de alta, modificar o eliminar trabajadores es delicado (aqui se decide
 * quien puede entrar en la aplicacion y con que rol), asi que esas tres
 * operaciones llevan @PreAuthorize("hasAuthority('ADMIN')") y solo las puede
 * hacer un trabajador con rol ADMIN. Se usa hasAuthority() y no hasRole()
 * porque Trabajador.getAuthorities() ya no anade el prefijo "ROLE_", asi que
 * hay que comparar el texto "ADMIN" tal cual. Listar y consultar uno se dejan
 * abiertos a cualquiera que haya iniciado sesion, como un listado interno de
 * companeros.
 *
 * @Valid activa las validaciones puestas en la entidad Trabajador (email con
 * formato correcto, contrasena de al menos 8 caracteres si se envia...).
 */
@RestController
@RequestMapping("/api/trabajador")
public class TrabajadorController {
    private final TrabajadorService servicio;

    public TrabajadorController(TrabajadorService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Trabajador> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Trabajador obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Trabajador anadir(@Valid @RequestBody Trabajador trabajador) {
        return servicio.guardar(trabajador);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Trabajador actualizar(@PathVariable Integer id, @Valid @RequestBody Trabajador trabajador) {
        return servicio.actualizar(id, trabajador);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
