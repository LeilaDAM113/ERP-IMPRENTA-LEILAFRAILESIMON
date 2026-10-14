package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Empresa;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.EmpresaRepository;

// Lo que se puede hacer con Empresa (lo usa EmpresaController)
@Service
public class EmpresaService {
    private final EmpresaRepository repositorio;

    public EmpresaService(EmpresaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Empresa> listar() {
        return repositorio.findAll();
    }

    public Empresa obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public Empresa guardar(Empresa objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public Empresa actualizar(Integer id, Empresa objeto) {
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
