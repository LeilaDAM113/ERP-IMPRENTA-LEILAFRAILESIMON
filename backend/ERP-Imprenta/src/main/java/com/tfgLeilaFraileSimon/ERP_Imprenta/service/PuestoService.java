package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PuestoRepository;

// Lo que se puede hacer con Puesto (lo usa PuestoController)
@Service
public class PuestoService {
    private final PuestoRepository repositorio;

    public PuestoService(PuestoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Puesto> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Puesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Puesto guardar(Puesto objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public Puesto actualizar(Integer id, Puesto objeto) {
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
