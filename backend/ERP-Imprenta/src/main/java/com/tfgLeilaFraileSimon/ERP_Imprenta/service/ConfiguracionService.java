package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Configuracion;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ConfiguracionRepository;

@Service
public class ConfiguracionService {
    private final ConfiguracionRepository repositorio;

    public ConfiguracionService(ConfiguracionRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Configuracion> listar() {
        return repositorio.findAll();
    }

    public Configuracion obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public Configuracion guardar(Configuracion objeto) {
        return repositorio.save(objeto);
    }

    public Configuracion actualizar(Integer id, Configuracion objeto) {
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