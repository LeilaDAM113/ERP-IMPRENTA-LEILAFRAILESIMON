package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Albaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.AlbaranRepository;

// servicio de Albaran
@Service
public class AlbaranService {
    private final AlbaranRepository repositorio;

    public AlbaranService(AlbaranRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Albaran> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Albaran obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Albaran guardar(Albaran objeto) {
        return repositorio.save(objeto);
    }

    // modificar uno
    public Albaran actualizar(Integer id, Albaran objeto) {
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
