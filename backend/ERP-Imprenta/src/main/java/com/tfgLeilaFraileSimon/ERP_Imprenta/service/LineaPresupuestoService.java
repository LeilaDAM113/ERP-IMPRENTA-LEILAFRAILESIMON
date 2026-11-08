package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LineaPresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPresupuestoRepository;

// servicio de LineaPresupuesto
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class LineaPresupuestoService {
    private final LineaPresupuestoRepository repositorio;
    private final Buscador buscador;

    public LineaPresupuestoService(LineaPresupuestoRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
        this.buscador = buscador;
    }

    // todos
    public List<LineaPresupuesto> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public LineaPresupuesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO
    public LineaPresupuesto guardar(LineaPresupuestoRequest datos) {
        LineaPresupuesto objeto = new LineaPresupuesto();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public LineaPresupuesto actualizar(Integer id, LineaPresupuestoRequest datos) {
        // si no existe da 404
        LineaPresupuesto objeto = repositorio.findById(id).orElseThrow();
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
    private void copiarDatos(LineaPresupuestoRequest datos, LineaPresupuesto objeto) {
        objeto.setPresupuesto(buscador.presupuesto(datos.idPresupuesto()));
        objeto.setProducto(buscador.producto(datos.idProducto()));
        objeto.setCantidad(datos.cantidad());
        objeto.setPrecioUnitario(datos.precioUnitario());
        objeto.setSubtotal(datos.subtotal());
        objeto.setTotal(datos.total());
        objeto.setCosteMaterial(datos.costeMaterial());
        objeto.setCosteMaquina(datos.costeMaquina());
        objeto.setCosteManoObra(datos.costeManoObra());
        objeto.setHorasEstimadas(datos.horasEstimadas());
        objeto.setMargen(datos.margen());
    }
}
