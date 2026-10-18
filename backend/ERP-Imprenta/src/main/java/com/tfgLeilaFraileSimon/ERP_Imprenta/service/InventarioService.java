package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.InventarioRepository;

// servicio de Inventario
@Service
public class InventarioService {
    private final InventarioRepository repositorio;

    public InventarioService(InventarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Inventario> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Inventario obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Inventario guardar(Inventario objeto) {
        return repositorio.save(objeto);
    }

       // modificar uno
    public Inventario actualizar(Integer id, Inventario objeto) {
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
