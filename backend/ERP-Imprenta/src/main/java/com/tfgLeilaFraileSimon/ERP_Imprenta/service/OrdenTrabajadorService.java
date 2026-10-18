package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajadorRepository;

// servicio de OrdenTrabajador
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

       // modificar uno
    public OrdenTrabajador actualizar(Integer id, OrdenTrabajador objeto) {
        // si no existe da 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // borrar uno
    public void eliminar(Integer id) {
        // si no existe da 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }
}
