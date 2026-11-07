package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ProductoProceso;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ProductoProcesoRepository;

@Service
public class ProductoProcesoService {
    private final ProductoProcesoRepository repositorio;

    public ProductoProcesoService(ProductoProcesoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ProductoProceso> listar() {
        return repositorio.findAll();
    }

    public ProductoProceso obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public ProductoProceso guardar(ProductoProceso objeto) {
        return repositorio.save(objeto);
    }

    public ProductoProceso actualizar(Integer id, ProductoProceso objeto) {
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