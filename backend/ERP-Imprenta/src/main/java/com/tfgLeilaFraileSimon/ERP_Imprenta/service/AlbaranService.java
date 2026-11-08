package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.AlbaranRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Albaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.AlbaranRepository;

// servicio de Albaran
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class AlbaranService {
    private final AlbaranRepository repositorio;
    private final Buscador buscador;

    public AlbaranService(AlbaranRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<Albaran> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Albaran obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public Albaran guardar(AlbaranRequest datos) {
        Albaran objeto = new Albaran();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public Albaran actualizar(Integer id, AlbaranRequest datos) {
        // si no existe da 404
        Albaran objeto = repositorio.findById(id).orElseThrow();
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
    private void copiarDatos(AlbaranRequest datos, Albaran objeto) {
        objeto.setNumero(datos.numero());
        objeto.setCliente(buscador.cliente(datos.idCliente()));
        objeto.setOrdenTrabajo(buscador.ordenTrabajo(datos.idOrdenTrabajo()));
        objeto.setTransportista(buscador.trabajador(datos.idTransportista()));
        objeto.setFechaEmision(datos.fechaEmision());
        objeto.setFechaEntrega(datos.fechaEntrega());
        objeto.setFirma(datos.firma());
        objeto.setNombreReceptor(datos.nombreReceptor());
        objeto.setObservaciones(datos.observaciones());
        objeto.setEstado(datos.estado());
    }
}
