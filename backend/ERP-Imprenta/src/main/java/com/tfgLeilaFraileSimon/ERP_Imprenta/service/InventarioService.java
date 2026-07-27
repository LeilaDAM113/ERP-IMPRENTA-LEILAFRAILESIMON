package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.InventarioRepository;

/*
 * SERVICIO CRUD DE Inventario. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class InventarioService {
    private final InventarioRepository repositorio;

    public InventarioService(InventarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Inventario> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Inventario obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Inventario guardar(Inventario objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Inventario actualizar(Integer id, Inventario objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
