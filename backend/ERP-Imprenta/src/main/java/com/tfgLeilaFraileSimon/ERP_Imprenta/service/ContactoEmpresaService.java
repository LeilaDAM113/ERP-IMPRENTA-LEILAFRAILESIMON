package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ContactoEmpresa;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ContactoEmpresaRepository;

/*
 * SERVICIO CRUD DE ContactoEmpresa. Contiene la logica de las 4 operaciones basicas.
 */
@Service
public class ContactoEmpresaService {
    private final ContactoEmpresaRepository repositorio;

    public ContactoEmpresaService(ContactoEmpresaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ContactoEmpresa> listar() {
        return repositorio.findAll();
    }

    public ContactoEmpresa obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public ContactoEmpresa guardar(ContactoEmpresa objeto) {
        return repositorio.save(objeto);
    }

    public ContactoEmpresa actualizar(Integer id, ContactoEmpresa objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
