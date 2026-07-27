package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PagoRepository;

/*
 * SERVICIO CRUD DE Pago. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class PagoService {
    private final PagoRepository repositorio;

    public PagoService(PagoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Pago> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Pago obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Pago guardar(Pago objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Pago actualizar(Integer id, Pago objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
