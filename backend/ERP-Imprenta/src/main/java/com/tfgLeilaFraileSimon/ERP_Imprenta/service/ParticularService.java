package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Particular;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ParticularRepository;

/*
 * SERVICIO CRUD DE Particular. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class ParticularService {
    private final ParticularRepository repositorio;

    public ParticularService(ParticularRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Particular> listar() {
        return repositorio.findAll();
    }

    public Particular obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public Particular guardar(Particular objeto) {
        return repositorio.save(objeto);
    }

    public Particular actualizar(Integer id, Particular objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
