package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.OrdenTrabajoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajoRepository;

// servicio de OrdenTrabajo
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class OrdenTrabajoService {
    private final OrdenTrabajoRepository repositorio;
    private final Buscador buscador;

    public OrdenTrabajoService(OrdenTrabajoRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<OrdenTrabajo> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public OrdenTrabajo obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public OrdenTrabajo guardar(OrdenTrabajoRequest datos) {
        OrdenTrabajo objeto = new OrdenTrabajo();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public OrdenTrabajo actualizar(Integer id, OrdenTrabajoRequest datos) {
        // si no existe da 404
        OrdenTrabajo objeto = repositorio.findById(id).orElseThrow();
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
    private void copiarDatos(OrdenTrabajoRequest datos, OrdenTrabajo objeto) {
        objeto.setNumero(datos.numero());
        objeto.setTitulo(datos.titulo());
        objeto.setDescripcion(datos.descripcion());
        objeto.setPresupuesto(buscador.presupuesto(datos.idPresupuesto()));
        objeto.setTrabajador(buscador.trabajador(datos.idTrabajador()));
        objeto.setFechaInicio(datos.fechaInicio());
        objeto.setFechaFin(datos.fechaFin());
        objeto.setFechaEntrega(datos.fechaEntrega());
        objeto.setPrioridad(datos.prioridad());
        objeto.setEstado(datos.estado());
    }
}
