package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenEmpleado;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenEmpleadoRepository;

/*
 * SERVICIO CRUD DE OrdenEmpleado. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class OrdenEmpleadoService {
    private final OrdenEmpleadoRepository repositorio;

    public OrdenEmpleadoService(OrdenEmpleadoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<OrdenEmpleado> listar() {
        return repositorio.findAll();
    }

    public OrdenEmpleado obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public OrdenEmpleado guardar(OrdenEmpleado objeto) {
        return repositorio.save(objeto);
    }

    public OrdenEmpleado actualizar(Integer id, OrdenEmpleado objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
