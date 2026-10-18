package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PedidoRepository;

// servicio de Pedido
@Service
public class PedidoService {
    private final PedidoRepository repositorio;

    public PedidoService(PedidoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Pedido> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Pedido obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Pedido guardar(Pedido objeto) {
        return repositorio.save(objeto);
    }

        // modificar uno
    public Pedido actualizar(Integer id, Pedido objeto) {
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
