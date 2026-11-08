package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;

// servicio de Presupuesto
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class PresupuestoService {
    private final PresupuestoRepository repositorio;
    private final Buscador buscador;

    public PresupuestoService(PresupuestoRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<Presupuesto> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Presupuesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public Presupuesto guardar(PresupuestoRequest datos) {
        Presupuesto objeto = new Presupuesto();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public Presupuesto actualizar(Integer id, PresupuestoRequest datos) {
        // si no existe da 404
        Presupuesto objeto = repositorio.findById(id).orElseThrow();
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
    private void copiarDatos(PresupuestoRequest datos, Presupuesto objeto) {
        objeto.setNumero(datos.numero());
        objeto.setCliente(buscador.cliente(datos.idCliente()));
        objeto.setComercial(buscador.trabajador(datos.idComercial()));
        objeto.setFechaEmision(datos.fechaEmision());
        objeto.setFechaEnvio(datos.fechaEnvio());
        objeto.setFechaValidez(datos.fechaValidez());
        objeto.setDescripcion(datos.descripcion());
        objeto.setSubtotal(datos.subtotal());
        objeto.setImporte(datos.importe());
        objeto.setPorcentajeIva(datos.porcentajeIva());
        objeto.setEstado(datos.estado());
    }
}
