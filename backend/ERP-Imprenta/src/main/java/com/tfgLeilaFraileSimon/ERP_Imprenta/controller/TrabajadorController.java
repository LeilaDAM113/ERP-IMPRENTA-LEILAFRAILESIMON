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

// endpoints de trabajadores
// crear, modificar y borrar solo lo puede hacer el admin
// uso hasAuthority y no hasRole porque mis roles no llevan ROLE_ delante
// uso DTOs para que la contraseña no salga nunca
// @Valid comprueba las reglas del DTO (email, contraseña de 8...)
@RestController
@RequestMapping("/api/trabajador")
public class TrabajadorController {
    private final TrabajadorService servicio;

    public TrabajadorController(TrabajadorService servicio) {
        this.servicio = servicio;
    }

    // lista por paginas de 30 en 30 (?page=1&size=30), siempre ordenada por id
    @GetMapping
    public PagedModel<TrabajadorResponse> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {
        // en la url empiezo en la pagina 1 pero spring en la 0, por eso resto 1
        // y no dejo pedir mas de 100 de golpe
        int tamano = Math.min(Math.max(size, 1), 100);
        Pageable paginacion = PageRequest.of(Math.max(page - 1, 0), tamano, Sort.by("id"));
        return new PagedModel<>(servicio.listar(paginacion).map(TrabajadorResponse::desde));
    }

    @GetMapping("/{id}")
    public TrabajadorResponse obtener(@PathVariable Integer id) {
        return TrabajadorResponse.desde(servicio.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public TrabajadorResponse anadir(@Valid @RequestBody TrabajadorCrearRequest datos) {
        return TrabajadorResponse.desde(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public TrabajadorResponse actualizar(@PathVariable Integer id,
            @Valid @RequestBody TrabajadorActualizarRequest datos) {
        return TrabajadorResponse.desde(servicio.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
    
}
