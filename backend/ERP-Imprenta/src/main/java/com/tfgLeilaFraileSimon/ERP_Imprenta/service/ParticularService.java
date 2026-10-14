package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Particular;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ParticularRepository;

// Lo que se puede hacer con Particular (lo usa ParticularController)
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

       // cambiar los datos de uno que ya existe
    public Particular actualizar(Integer id, Particular objeto) {
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
