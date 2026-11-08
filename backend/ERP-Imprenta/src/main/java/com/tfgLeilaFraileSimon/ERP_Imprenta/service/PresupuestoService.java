package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoOrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.PrioridadOrden;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPresupuestoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;

// servicio de Presupuesto
// recibe el DTO, busca las cosas relacionadas por su id y guarda
// y tiene las acciones: enviar, aceptar (crea la orden de trabajo) y rechazar
@Service
public class PresupuestoService {
    private final PresupuestoRepository repositorio;
    private final OrdenTrabajoRepository ordenTrabajoRepositorio;
    private final Buscador buscador;
    private final CalculoService calculo;
    private final LineaPresupuestoRepository lineaRepositorio;
    private final PdfService pdfService;

    public PresupuestoService(PresupuestoRepository repositorio, OrdenTrabajoRepository ordenTrabajoRepositorio,
            Buscador buscador, CalculoService calculo, LineaPresupuestoRepository lineaRepositorio,
            PdfService pdfService) {
        this.repositorio = repositorio;
        this.ordenTrabajoRepositorio = ordenTrabajoRepositorio;
        this.buscador = buscador;
        this.calculo = calculo;
        this.lineaRepositorio = lineaRepositorio;
        this.pdfService = pdfService;
    }

    // todos
    public List<Presupuesto> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Presupuesto obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo con los datos del DTO. Siempre empieza en BORRADOR
    public Presupuesto guardar(PresupuestoRequest datos) {
        Presupuesto objeto = new Presupuesto();
        copiarDatos(datos, objeto);
        objeto.setEstado(EstadoPresupuesto.BORRADOR);
        // todavia no tiene lineas: empieza en 0 (y el IVA al 21% si no se dice otro)
        calculo.aplicarIva(objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos.
    // solo se puede si esta en BORRADOR (lo que ya se ha mandado al cliente no se toca)
    public Presupuesto actualizar(Integer id, PresupuestoRequest datos) {
        // si no existe da 404
        Presupuesto objeto = repositorio.findById(id).orElseThrow();
        comprobarEstado(objeto, EstadoPresupuesto.BORRADOR, "modificar");
        copiarDatos(datos, objeto);
        // por si ha cambiado el IVA, vuelvo a calcular el importe
        calculo.aplicarIva(objeto);
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

    // el presupuesto en PDF, con sus lineas
    public byte[] pdf(Integer id) {
        Presupuesto presupuesto = obtener(id);
        return pdfService.presupuesto(presupuesto, lineaRepositorio.findByPresupuesto(presupuesto));
    }

    // ---- ACCIONES (los botones del presupuesto) ----

    // enviar: BORRADOR -> ENVIADO, y apunto la fecha de hoy
    public Presupuesto enviar(Integer id) {
        Presupuesto presupuesto = obtener(id);
        comprobarEstado(presupuesto, EstadoPresupuesto.BORRADOR, "enviar");
        presupuesto.setEstado(EstadoPresupuesto.ENVIADO);
        presupuesto.setFechaEnvio(LocalDate.now());
        return repositorio.save(presupuesto);
    }

    // rechazar: el cliente dice que no. ENVIADO -> RECHAZADO
    public Presupuesto rechazar(Integer id) {
        Presupuesto presupuesto = obtener(id);
        comprobarEstado(presupuesto, EstadoPresupuesto.ENVIADO, "rechazar");
        presupuesto.setEstado(EstadoPresupuesto.RECHAZADO);
        return repositorio.save(presupuesto);
    }

    // aceptar: el cliente dice que si. ENVIADO -> ACEPTADO
    // y se crea sola la orden de trabajo para el taller.
    // @Transactional: o se hacen las dos cosas o ninguna (si falla algo, no se queda a medias)
    @Transactional
    public OrdenTrabajo aceptar(Integer id) {
        Presupuesto presupuesto = obtener(id);
        comprobarEstado(presupuesto, EstadoPresupuesto.ENVIADO, "aceptar");
        presupuesto.setEstado(EstadoPresupuesto.ACEPTADO);
        repositorio.save(presupuesto);

        OrdenTrabajo orden = new OrdenTrabajo();
        // el numero sale del id del presupuesto: OT-0001, OT-0002...
        orden.setNumero(String.format("OT-%04d", presupuesto.getId()));
        orden.setTitulo("Presupuesto " + presupuesto.getNumero());
        orden.setDescripcion(presupuesto.getDescripcion());
        orden.setPresupuesto(presupuesto);
        orden.setPrioridad(PrioridadOrden.MEDIA);
        orden.setEstado(EstadoOrdenTrabajo.PENDIENTE);
        return ordenTrabajoRepositorio.save(orden);
    }

    // si el presupuesto no esta en el estado que hace falta, no dejo hacerlo (409)
    private void comprobarEstado(Presupuesto presupuesto, EstadoPresupuesto necesario, String accion) {
        if (presupuesto.getEstado() != necesario) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede " + accion + " un presupuesto en " + necesario
                            + " y este esta en " + presupuesto.getEstado());
        }
    }

    // paso los datos del DTO al objeto (lo uso al crear y al modificar)
    private void copiarDatos(PresupuestoRequest datos, Presupuesto objeto) {
        objeto.setNumero(datos.numero());
        objeto.setCliente(buscador.cliente(datos.idCliente()));
        objeto.setComercial(buscador.trabajador(datos.idComercial()));
        objeto.setFechaEmision(datos.fechaEmision());
        objeto.setFechaValidez(datos.fechaValidez());
        objeto.setDescripcion(datos.descripcion());
        objeto.setPorcentajeIva(datos.porcentajeIva());
    }
}
