package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajoRepository;

/*
 * SERVICIO CRUD DE OrdenTrabajo. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class OrdenTrabajoService {
    private final OrdenTrabajoRepository repositorio;

    public OrdenTrabajoService(OrdenTrabajoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<OrdenTrabajo> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public OrdenTrabajo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public OrdenTrabajo guardar(OrdenTrabajo objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public OrdenTrabajo actualizar(Integer id, OrdenTrabajo objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
