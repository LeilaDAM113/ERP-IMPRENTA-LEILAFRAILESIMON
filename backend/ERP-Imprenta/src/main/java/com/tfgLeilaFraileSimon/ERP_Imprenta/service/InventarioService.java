package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.InventarioRepository;

// Lo que se puede hacer con Inventario (lo usa InventarioController)
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

       // cambiar los datos de uno que ya existe
    public Inventario actualizar(Integer id, Inventario objeto) {
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
