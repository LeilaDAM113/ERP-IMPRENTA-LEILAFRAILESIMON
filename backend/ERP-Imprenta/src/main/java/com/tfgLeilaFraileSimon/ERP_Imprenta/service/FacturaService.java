package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.FacturaRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.FacturaRepository;

// servicio de Factura
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class FacturaService {
    private final FacturaRepository repositorio;
    private final Buscador buscador;

    public FacturaService(FacturaRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<Factura> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Factura obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public Factura guardar(FacturaRequest datos) {
        Factura objeto = new Factura();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public Factura actualizar(Integer id, FacturaRequest datos) {
        // si no existe da 404
        Factura objeto = repositorio.findById(id).orElseThrow();
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
    private void copiarDatos(FacturaRequest datos, Factura objeto) {
        objeto.setNumero(datos.numero());
        objeto.setCliente(buscador.cliente(datos.idCliente()));
        objeto.setOrdenTrabajo(buscador.ordenTrabajo(datos.idOrdenTrabajo()));
        objeto.setFechaEmision(datos.fechaEmision());
        objeto.setFechaVencimiento(datos.fechaVencimiento());
        objeto.setFechaPago(datos.fechaPago());
        objeto.setMetodoPago(datos.metodoPago());
        objeto.setEstado(datos.estado());
        objeto.setBaseImponible(datos.baseImponible());
        objeto.setPorcentajeIva(datos.porcentajeIva());
        objeto.setCuotaIva(datos.cuotaIva());
        objeto.setTotal(datos.total());
    }
}
