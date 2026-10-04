package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaFacturaRepository;

/*
 * SERVICIO CRUD DE LineaFactura. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class LineaFacturaService {
    private final LineaFacturaRepository repositorio;

    public LineaFacturaService(LineaFacturaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<LineaFactura> listar() {
        return repositorio.findAll();
    }

    public LineaFactura obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public LineaFactura guardar(LineaFactura objeto) {
        return repositorio.save(objeto);
    }

    public LineaFactura actualizar(Integer id, LineaFactura objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
