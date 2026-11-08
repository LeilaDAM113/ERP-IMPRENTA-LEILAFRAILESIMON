package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.RegistroTiempoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RegistroTiempo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.RegistroTiempoRepository;

// servicio de RegistroTiempo
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class RegistroTiempoService {
    private final RegistroTiempoRepository repositorio;
    private final Buscador buscador;

    public RegistroTiempoService(RegistroTiempoRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<RegistroTiempo> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public RegistroTiempo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public RegistroTiempo guardar(RegistroTiempoRequest datos) {
        RegistroTiempo objeto = new RegistroTiempo();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public RegistroTiempo actualizar(Integer id, RegistroTiempoRequest datos) {
        // si no existe da 404
        RegistroTiempo objeto = repositorio.findById(id).orElseThrow();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // borrar uno
    public void eliminar(Integer id) {
        // si no existe da 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }

    // paso los datos del DTO al objeto (lo uso al crear y al modificar)
    private void copiarDatos(RegistroTiempoRequest datos, RegistroTiempo objeto) {
        objeto.setOrdenTrabajo(buscador.ordenTrabajo(datos.idOrdenTrabajo()));
        objeto.setTrabajador(buscador.trabajador(datos.idTrabajador()));
        objeto.setMaquina(buscador.maquina(datos.idMaquina()));
        objeto.setInicio(datos.inicio());
        objeto.setFin(datos.fin());
        objeto.setMinutos(datos.minutos());
        objeto.setObservaciones(datos.observaciones());
    }
}
