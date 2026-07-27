package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PuestoRepository;

/*
 * SERVICIO CRUD DE Puesto. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class PuestoService {
    private final PuestoRepository repositorio;

    public PuestoService(PuestoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Puesto> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Puesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Puesto guardar(Puesto objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Puesto actualizar(Integer id, Puesto objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
