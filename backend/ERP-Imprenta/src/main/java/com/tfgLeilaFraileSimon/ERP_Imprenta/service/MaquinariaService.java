package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Maquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.MaquinariaRepository;

// Lo que se puede hacer con Maquinaria (lo usa MaquinariaController)
@Service
public class MaquinariaService {
    private final MaquinariaRepository repositorio;

    public MaquinariaService(MaquinariaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Maquinaria> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Maquinaria obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Maquinaria guardar(Maquinaria objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public Maquinaria actualizar(Integer id, Maquinaria objeto) {
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
