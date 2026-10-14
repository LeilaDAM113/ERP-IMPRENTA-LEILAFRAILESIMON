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

// Endpoints de los trabajadores: ver todos, ver uno, crear, modificar y borrar.
// Crear, modificar y borrar solo lo puede hacer el ADMIN, porque aqui se decide
// quien entra en la aplicacion y con que rol.
// Uso hasAuthority('ADMIN') y no hasRole porque mis roles no llevan "ROLE_" delante.
// Recibo y devuelvo DTOs, asi la contraseña nunca sale en las respuestas.
// @Valid hace que se comprueben las reglas del DTO (email bien escrito, contraseña de 8...)
@RestController
@RequestMapping("/api/trabajador")
public class TrabajadorController {
    private final TrabajadorService servicio;

    public TrabajadorController(TrabajadorService servicio) {
        this.servicio = servicio;
    }

    // Lista de trabajadores por paginas, de 30 en 30 (?page=1&size=30).
    // Solo dejo elegir la pagina y el tamaño, y siempre ordeno por id,
    // para que nadie pueda ordenar por campos raros como la contraseña.
    @GetMapping
    public PagedModel<TrabajadorResponse> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {
        // en la URL la primera pagina es la 1, pero Spring empieza a contar en 0,
        // por eso le resto 1 (y nunca bajo de 0).
        // el tamaño lo dejo entre 1 y 100 para que nadie pida miles de golpe
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
