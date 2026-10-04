package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPresupuestoRepository;

/*
 * SERVICIO CRUD DE LineaPresupuesto. Contiene la logica de las 4 operaciones basicas.
 */
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

    public LineaPresupuesto actualizar(Integer id, LineaPresupuesto objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
