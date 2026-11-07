package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPedidoRepository;

@Service
public class LineaPedidoService {
    private final LineaPedidoRepository repositorio;

    public LineaPedidoService(LineaPedidoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<LineaPedido> listar() {
        return repositorio.findAll();
    }

    public LineaPedido obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public LineaPedido guardar(LineaPedido objeto) {
        return repositorio.save(objeto);
    }

    public LineaPedido actualizar(Integer id, LineaPedido objeto) {
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }
}