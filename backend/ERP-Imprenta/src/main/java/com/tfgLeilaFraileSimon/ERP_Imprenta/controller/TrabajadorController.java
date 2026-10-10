package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorActualizarRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorCrearRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorResponse;
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
 * La API trabaja con DTOs (paquete dto), no con la entidad Trabajador:
 *  - Se RECIBE TrabajadorCrearRequest / TrabajadorActualizarRequest, que no
 *    tienen id (lo genera la base de datos o lo indica la URL).
 *  - Se DEVUELVE TrabajadorResponse, sin contrasena ni los campos internos de
 *    Spring Security (UserDetails).
 *
 * @Valid activa las validaciones puestas en los DTOs (email con formato
 * correcto, contrasena obligatoria al crear, al menos 8 caracteres...).
 */
@RestController
@RequestMapping("/api/trabajador")
public class TrabajadorController {
    private final TrabajadorService servicio;

    public TrabajadorController(TrabajadorService servicio) {
        this.servicio = servicio;
    }

    // LISTAR paginado: por defecto los 30 primeros (?page=0&size=30).
    // No se usa Pageable como parametro porque eso permitiria al cliente
    // mandar ?sort=cualquierCampo (incluso ?sort=password). Aqui solo se
    // aceptan page y size, y el orden es siempre por id.
    @GetMapping
    public PagedModel<TrabajadorResponse> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {
        // En la URL la primera pagina es la 1 (?page=1), pero en Spring las
        // paginas empiezan en 0, por eso se resta 1 al crear el PageRequest.
        // OJO: en la respuesta, page.number sigue contando desde 0 (al pedir
        // ?page=1 devuelve "number": 0).
        // Se limita size (entre 1 y 100) para que nadie pida 100000 de golpe.
        // page - 1 pasa a la numeracion de Spring, y Math.max(..., 0) evita
        // un indice negativo si llega ?page=0 o menos (seria un error 500).
        int tamano = Math.min(Math.max(size, 1), 100);
        Pageable paginacion = PageRequest.of(Math.max(page - 1, 0), tamano, Sort.by("id"));
        return new PagedModel<>(servicio.listar(paginacion).map(TrabajadorResponse::desde));
    }

    @GetMapping("/{id}")
    public TrabajadorResponse obtener(@PathVariable Integer id) {
        return TrabajadorResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('ADMIN')")
    public TrabajadorResponse anadir(@Valid @RequestBody TrabajadorCrearRequest datos) {
        return TrabajadorResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public TrabajadorResponse actualizar(@PathVariable Integer id,
            @Valid @RequestBody TrabajadorActualizarRequest datos) {
        return TrabajadorResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
    
}
