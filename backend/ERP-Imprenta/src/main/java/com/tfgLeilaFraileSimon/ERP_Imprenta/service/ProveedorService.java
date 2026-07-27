package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Proveedor;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ProveedorRepository;

/*
 * SERVICIO CRUD DE Proveedor. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class ProveedorService {
    private final ProveedorRepository repositorio;

    public ProveedorService(ProveedorRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Proveedor> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Proveedor obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Proveedor guardar(Proveedor objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Proveedor actualizar(Integer id, Proveedor objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
