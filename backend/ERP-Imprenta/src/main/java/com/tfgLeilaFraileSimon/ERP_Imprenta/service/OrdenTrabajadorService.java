package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajadorRepository;

/*
 * SERVICIO CRUD DE OrdenTrabajador. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class OrdenTrabajadorService {
    private final OrdenTrabajadorRepository repositorio;

    public OrdenTrabajadorService(OrdenTrabajadorRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<OrdenTrabajador> listar() {
        return repositorio.findAll();
    }

    public OrdenTrabajador obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public OrdenTrabajador guardar(OrdenTrabajador objeto) {
        return repositorio.save(objeto);
    }

    public OrdenTrabajador actualizar(Integer id, OrdenTrabajador objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
