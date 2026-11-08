package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PagoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PagoRepository;

// servicio de Pago
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class PagoService {
    private final PagoRepository repositorio;
    private final Buscador buscador;

    public PagoService(PagoRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<Pago> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Pago obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public Pago guardar(PagoRequest datos) {
        Pago objeto = new Pago();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public Pago actualizar(Integer id, PagoRequest datos) {
        // si no existe da 404
        Pago objeto = repositorio.findById(id).orElseThrow();
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
    private void copiarDatos(PagoRequest datos, Pago objeto) {
        objeto.setFactura(buscador.factura(datos.idFactura()));
        objeto.setDescripcion(datos.descripcion());
        objeto.setFechaEmision(datos.fechaEmision());
        objeto.setFechaPago(datos.fechaPago());
        objeto.setMetodoPago(datos.metodoPago());
        objeto.setImporte(datos.importe());
        objeto.setEstado(datos.estado());
    }
}
