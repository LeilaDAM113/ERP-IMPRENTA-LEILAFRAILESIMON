package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;

/*
 * SERVICIO CRUD DE Presupuesto. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class PresupuestoService {
    private final PresupuestoRepository repositorio;

    public PresupuestoService(PresupuestoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Presupuesto> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Presupuesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Presupuesto guardar(Presupuesto objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Presupuesto actualizar(Integer id, Presupuesto objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
