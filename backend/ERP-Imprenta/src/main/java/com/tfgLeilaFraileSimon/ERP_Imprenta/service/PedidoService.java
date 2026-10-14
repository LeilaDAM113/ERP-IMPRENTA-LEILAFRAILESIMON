package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PedidoRepository;

// Lo que se puede hacer con Pedido (lo usa PedidoController)
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

        // cambiar los datos de uno que ya existe
    public Pedido actualizar(Integer id, Pedido objeto) {
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
