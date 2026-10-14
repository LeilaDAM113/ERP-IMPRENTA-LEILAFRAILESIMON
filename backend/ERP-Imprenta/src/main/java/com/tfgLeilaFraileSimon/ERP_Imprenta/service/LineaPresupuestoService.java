package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPresupuestoRepository;

// Lo que se puede hacer con LineaPresupuesto (lo usa LineaPresupuestoController)
@Service
public class LineaPresupuestoService {
    private final LineaPresupuestoRepository repositorio;

    public LineaPresupuestoService(LineaPresupuestoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<LineaPresupuesto> listar() {
        return repositorio.findAll();
    }

    public LineaPresupuesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public LineaPresupuesto guardar(LineaPresupuesto objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public LineaPresupuesto actualizar(Integer id, LineaPresupuesto objeto) {
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
