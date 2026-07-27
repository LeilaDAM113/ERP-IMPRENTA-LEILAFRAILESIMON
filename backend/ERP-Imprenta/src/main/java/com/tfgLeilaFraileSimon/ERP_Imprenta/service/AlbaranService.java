package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Albaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.AlbaranRepository;

/*
 * SERVICIO CRUD DE Albaran. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class AlbaranService {
    private final AlbaranRepository repositorio;

    public AlbaranService(AlbaranRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Albaran> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Albaran obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Albaran guardar(Albaran objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Albaran actualizar(Integer id, Albaran objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
