package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ContactoEmpresa;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ContactoEmpresaRepository;

// Lo que se puede hacer con ContactoEmpresa (lo usa ContactoEmpresaController)
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

       // cambiar los datos de uno que ya existe
    public ContactoEmpresa actualizar(Integer id, ContactoEmpresa objeto) {
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
