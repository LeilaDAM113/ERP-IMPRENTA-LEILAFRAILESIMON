package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaAlbaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaAlbaranRepository;

// Lo que se puede hacer con LineaAlbaran (lo usa LineaAlbaranController)
@Service
public class LineaAlbaranService {
    private final LineaAlbaranRepository repositorio;

    public LineaAlbaranService(LineaAlbaranRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<LineaAlbaran> listar() {
        return repositorio.findAll();
    }

    public LineaAlbaran obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public LineaAlbaran guardar(LineaAlbaran objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public LineaAlbaran actualizar(Integer id, LineaAlbaran objeto) {
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
