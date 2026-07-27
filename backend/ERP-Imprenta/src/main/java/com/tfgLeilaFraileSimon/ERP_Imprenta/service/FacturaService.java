package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.FacturaRepository;

/*
 * SERVICIO CRUD DE Factura. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class FacturaService {
    private final FacturaRepository repositorio;

    public FacturaService(FacturaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Factura> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Factura obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Factura guardar(Factura objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Factura actualizar(Integer id, Factura objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
