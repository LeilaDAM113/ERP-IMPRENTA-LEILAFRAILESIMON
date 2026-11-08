package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LineaPresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPresupuestoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;

// servicio de LineaPresupuesto
// cada vez que se crea, cambia o borra una linea: calculo su precio
// y vuelvo a sumar el total del presupuesto (CalculoService)
@Service
public class LineaPresupuestoService {
    private final LineaPresupuestoRepository repositorio;
    private final PresupuestoRepository presupuestoRepositorio;
    private final CalculoService calculo;
    private final Buscador buscador;

    public LineaPresupuestoService(LineaPresupuestoRepository repositorio, PresupuestoRepository presupuestoRepositorio,
            CalculoService calculo, Buscador buscador) {
        this.repositorio = repositorio;
        this.presupuestoRepositorio = presupuestoRepositorio;
        this.calculo = calculo;
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

    // linea nueva: calculo su precio y el total del presupuesto
    @Transactional
    public LineaPresupuesto guardar(LineaPresupuestoRequest datos) {
        LineaPresupuesto linea = new LineaPresupuesto();
        copiarDatos(datos, linea);
        comprobarBorrador(linea.getPresupuesto());
        calculo.calcularLinea(linea);
        repositorio.save(linea);
        recalcularPresupuesto(linea.getPresupuesto());
        return linea;
    }

    // modificar una linea: igual, y si se ha cambiado de presupuesto recalculo los dos
    @Transactional
    public LineaPresupuesto actualizar(Integer id, LineaPresupuestoRequest datos) {
        // si no existe da 404
        LineaPresupuesto linea = repositorio.findById(id).orElseThrow();
        Presupuesto antes = linea.getPresupuesto();
        comprobarBorrador(antes);
        copiarDatos(datos, linea);
        comprobarBorrador(linea.getPresupuesto());
        calculo.calcularLinea(linea);
        repositorio.save(linea);
        recalcularPresupuesto(linea.getPresupuesto());
        if (antes != null && !antes.getId().equals(linea.getPresupuesto().getId())) {
            recalcularPresupuesto(antes);
        }
        return linea;
    }

    // borrar una linea y volver a sumar el presupuesto
    @Transactional
    public void eliminar(Integer id) {
        // si no existe da 404
        LineaPresupuesto linea = repositorio.findById(id).orElseThrow();
        Presupuesto presupuesto = linea.getPresupuesto();
        comprobarBorrador(presupuesto);
        repositorio.delete(linea);
        recalcularPresupuesto(presupuesto);
    }

    // sumo todas las lineas del presupuesto y le pongo el IVA
    private void recalcularPresupuesto(Presupuesto presupuesto) {
        if (presupuesto == null) {
            return;
        }
        calculo.calcularPresupuesto(presupuesto, repositorio.findByPresupuesto(presupuesto));
        presupuestoRepositorio.save(presupuesto);
    }

    // las lineas solo se pueden tocar mientras el presupuesto esta en BORRADOR (si no, 409)
    private void comprobarBorrador(Presupuesto presupuesto) {
        if (presupuesto != null && presupuesto.getEstado() != EstadoPresupuesto.BORRADOR) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se pueden cambiar las lineas de un presupuesto en BORRADOR y este esta en "
                            + presupuesto.getEstado());
        }
    }

    // paso los datos del DTO al objeto (lo uso al crear y al modificar)
    private void copiarDatos(LineaPresupuestoRequest datos, LineaPresupuesto linea) {
        linea.setPresupuesto(buscador.presupuesto(datos.idPresupuesto()));
        linea.setProducto(buscador.producto(datos.idProducto()));
        linea.setCantidad(datos.cantidad());
        linea.setPrecioUnitario(datos.precioUnitario());
        linea.setCosteMaterial(datos.costeMaterial());
        linea.setCosteMaquina(datos.costeMaquina());
        linea.setCosteManoObra(datos.costeManoObra());
        linea.setHorasEstimadas(datos.horasEstimadas());
        linea.setMargen(datos.margen());
    }
}
