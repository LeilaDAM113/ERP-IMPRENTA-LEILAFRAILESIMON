package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Maquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.MaquinariaRepository;

/*
 * SERVICIO CRUD DE Maquinaria. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class MaquinariaService {
    private final MaquinariaRepository repositorio;

    public MaquinariaService(MaquinariaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Maquinaria> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Maquinaria obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Maquinaria guardar(Maquinaria objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Maquinaria actualizar(Integer id, Maquinaria objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
