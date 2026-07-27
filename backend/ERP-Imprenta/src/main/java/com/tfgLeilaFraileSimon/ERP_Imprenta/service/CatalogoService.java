package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.CatalogoRepository;

/*
 * SERVICIO CRUD DE Catalogo. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class CatalogoService {
    private final CatalogoRepository repositorio;

    public CatalogoService(CatalogoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Catalogo> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Catalogo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Catalogo guardar(Catalogo objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Catalogo actualizar(Integer id, Catalogo objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
