package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaFacturaRepository;

// servicio de LineaFactura
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

        // modificar uno
    public LineaFactura actualizar(Integer id, LineaFactura objeto) {
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
