package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaAlbaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaAlbaranRepository;

/*
 * SERVICIO CRUD DE LineaAlbaran. Contiene la logica de las 4 operaciones basicas.
 */
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

    public LineaAlbaran actualizar(Integer id, LineaAlbaran objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
