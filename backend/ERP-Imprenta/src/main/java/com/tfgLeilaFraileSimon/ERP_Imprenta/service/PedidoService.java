package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PedidoRepository;

/*
 * SERVICIO CRUD DE Pedido. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class PedidoService {
    private final PedidoRepository repositorio;

    public PedidoService(PedidoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Pedido> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Pedido obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Pedido guardar(Pedido objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Pedido actualizar(Integer id, Pedido objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
