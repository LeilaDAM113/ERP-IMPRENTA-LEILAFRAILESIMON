package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Aviso;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.AvisoRepository;

@Service
public class AvisoService {
    private final AvisoRepository repositorio;

    public AvisoService(AvisoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Aviso> listar() {
        return repositorio.findAll();
    }

    public Aviso obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public Aviso guardar(Aviso objeto) {
        return repositorio.save(objeto);
    }

    public Aviso actualizar(Integer id, Aviso objeto) {
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }
}