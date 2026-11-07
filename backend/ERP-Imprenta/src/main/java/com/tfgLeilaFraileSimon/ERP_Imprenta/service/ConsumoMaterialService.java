package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ConsumoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ConsumoMaterialRepository;

@Service
public class ConsumoMaterialService {
    private final ConsumoMaterialRepository repositorio;

    public ConsumoMaterialService(ConsumoMaterialRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ConsumoMaterial> listar() {
        return repositorio.findAll();
    }

    public ConsumoMaterial obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public ConsumoMaterial guardar(ConsumoMaterial objeto) {
        return repositorio.save(objeto);
    }

    public ConsumoMaterial actualizar(Integer id, ConsumoMaterial objeto) {
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