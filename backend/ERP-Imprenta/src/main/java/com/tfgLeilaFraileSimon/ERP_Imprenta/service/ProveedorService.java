package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Proveedor;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ProveedorRepository;

// Lo que se puede hacer con Proveedor (lo usa ProveedorController)
@Service
public class ProveedorService {
    private final ProveedorRepository repositorio;

    public ProveedorService(ProveedorRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Proveedor> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Proveedor obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Proveedor guardar(Proveedor objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public Proveedor actualizar(Integer id, Proveedor objeto) {
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
