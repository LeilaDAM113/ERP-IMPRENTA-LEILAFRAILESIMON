package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ClienteRepository;

// servicio de Cliente
@Service
public class ClienteService {
    private final ClienteRepository repositorio;

    public ClienteService(ClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Cliente> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Cliente obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Cliente guardar(Cliente objeto) {
        return repositorio.save(objeto);
    }

        // modificar uno
    public Cliente actualizar(Integer id, Cliente objeto) {
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
