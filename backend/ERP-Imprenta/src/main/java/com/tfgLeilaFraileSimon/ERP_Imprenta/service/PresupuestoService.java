package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;

// Lo que se puede hacer con Presupuesto (lo usa PresupuestoController)
@Service
public class PresupuestoService {
    private final PresupuestoRepository repositorio;

    public PresupuestoService(PresupuestoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Presupuesto> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Presupuesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Presupuesto guardar(Presupuesto objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public Presupuesto actualizar(Integer id, Presupuesto objeto) {
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
