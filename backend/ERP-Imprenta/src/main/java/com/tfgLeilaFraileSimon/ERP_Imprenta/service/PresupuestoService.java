package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;

// servicio de Presupuesto
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

       // modificar uno
    public Presupuesto actualizar(Integer id, Presupuesto objeto) {
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
