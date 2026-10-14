package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajoRepository;

// Lo que se puede hacer con OrdenTrabajo (lo usa OrdenTrabajoController)
@Service
public class OrdenTrabajoService {
    private final OrdenTrabajoRepository repositorio;

    public OrdenTrabajoService(OrdenTrabajoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<OrdenTrabajo> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public OrdenTrabajo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public OrdenTrabajo guardar(OrdenTrabajo objeto) {
        return repositorio.save(objeto);
    }

        // cambiar los datos de uno que ya existe
    public OrdenTrabajo actualizar(Integer id, OrdenTrabajo objeto) {
        // si no existe ese id, error 404 (antes creaba uno nuevo sin querer)
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // borrar uno
    public void eliminar(Integer id) {
        // si no existe ese id, error 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }
}
