package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajadorRepository;

// Lo que se puede hacer con OrdenTrabajador (lo usa OrdenTrabajadorController)
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

       // cambiar los datos de uno que ya existe
    public OrdenTrabajador actualizar(Integer id, OrdenTrabajador objeto) {
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
