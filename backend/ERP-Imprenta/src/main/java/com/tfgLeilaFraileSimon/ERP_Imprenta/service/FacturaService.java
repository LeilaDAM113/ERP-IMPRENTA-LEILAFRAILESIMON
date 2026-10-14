package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.FacturaRepository;

// Lo que se puede hacer con Factura (lo usa FacturaController)
@Service
public class FacturaService {
    private final FacturaRepository repositorio;

    public FacturaService(FacturaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Factura> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Factura obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Factura guardar(Factura objeto) {
        return repositorio.save(objeto);
    }

        // cambiar los datos de uno que ya existe
    public Factura actualizar(Integer id, Factura objeto) {
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
