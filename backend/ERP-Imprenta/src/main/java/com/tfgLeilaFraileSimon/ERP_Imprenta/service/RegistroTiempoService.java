package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RegistroTiempo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.RegistroTiempoRepository;

@Service
public class RegistroTiempoService {
    private final RegistroTiempoRepository repositorio;

    public RegistroTiempoService(RegistroTiempoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<RegistroTiempo> listar() {
        return repositorio.findAll();
    }

    public RegistroTiempo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public RegistroTiempo guardar(RegistroTiempo objeto) {
        return repositorio.save(objeto);
    }

    public RegistroTiempo actualizar(Integer id, RegistroTiempo objeto) {
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