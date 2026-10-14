package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.CatalogoRepository;

// Lo que se puede hacer con Catalogo (lo usa CatalogoController)
@Service
public class CatalogoService {
    private final CatalogoRepository repositorio;

    public CatalogoService(CatalogoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Catalogo> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Catalogo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Catalogo guardar(Catalogo objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public Catalogo actualizar(Integer id, Catalogo objeto) {
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
