package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Empresa;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.EmpresaRepository;

/*
 * SERVICIO CRUD DE Empresa. Contiene la logica de las 4 operaciones basicas.
 */
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

    public Empresa actualizar(Integer id, Empresa objeto) {
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
