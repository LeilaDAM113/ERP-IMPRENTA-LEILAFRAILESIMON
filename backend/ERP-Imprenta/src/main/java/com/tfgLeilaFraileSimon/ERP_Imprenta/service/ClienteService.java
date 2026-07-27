package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ClienteRepository;

/*
 * SERVICIO CRUD DE Cliente. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class ClienteService {
    private final ClienteRepository repositorio;

    public ClienteService(ClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    // LISTAR todos
    public List<Cliente> listar() {
        return repositorio.findAll();
    }

    // OBTENER uno por id
    public Cliente obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR uno nuevo
    public Cliente guardar(Cliente objeto) {
        return repositorio.save(objeto);
    }

    // ACTUALIZAR el de ese id con los datos recibidos
    public Cliente actualizar(Integer id, Cliente objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // ELIMINAR por id
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
