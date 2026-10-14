package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaFacturaRepository;

// Lo que se puede hacer con LineaFactura (lo usa LineaFacturaController)
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

        // cambiar los datos de uno que ya existe
    public LineaFactura actualizar(Integer id, LineaFactura objeto) {
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
