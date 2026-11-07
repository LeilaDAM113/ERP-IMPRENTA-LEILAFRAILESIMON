package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ProductoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ProductoMaterialRepository;

@Service
public class ProductoMaterialService {
    private final ProductoMaterialRepository repositorio;

    public ProductoMaterialService(ProductoMaterialRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ProductoMaterial> listar() {
        return repositorio.findAll();
    }

    public ProductoMaterial obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public ProductoMaterial guardar(ProductoMaterial objeto) {
        return repositorio.save(objeto);
    }

    public ProductoMaterial actualizar(Integer id, ProductoMaterial objeto) {
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