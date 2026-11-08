package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.FacturaRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.MorosoResponse;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.Resumen;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.FacturaRepository;

// servicio de Factura
// recibe el DTO, busca las cosas relacionadas por su id y guarda.
// calcula sola la fecha de vencimiento y saca las facturas vencidas y los morosos
@Service
public class FacturaService {
    // facturas que todavia no se han pagado
    private static final List<EstadoFactura> SIN_PAGAR = List.of(EstadoFactura.PENDIENTE, EstadoFactura.VENCIDA);

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
        completarFechas(objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public Factura actualizar(Integer id, FacturaRequest datos) {
        // si no existe da 404
        Factura objeto = repositorio.findById(id).orElseThrow();
        copiarDatos(datos, objeto);
        completarFechas(objeto);
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
    // ---- MOROSOS ----

    // facturas sin pagar cuya fecha de vencimiento ya ha pasado (la mas antigua primero)
    public List<Factura> vencidas() {
        return repositorio.findByEstadoInAndFechaVencimientoBeforeOrderByFechaVencimientoAsc(SIN_PAGAR,
                LocalDate.now());
    }

    // los clientes que deben dinero, con sus facturas vencidas juntas
    public List<MorosoResponse> morosos() {
        // 1. junto las facturas vencidas de cada cliente (la clave es el id del cliente)
        Map<Integer, List<Factura>> porCliente = new LinkedHashMap<>();
        for (Factura factura : vencidas()) {
            if (factura.getCliente() != null) {
                porCliente.computeIfAbsent(factura.getCliente().getId(), id -> new ArrayList<>()).add(factura);
            }
        }
        // 2. de cada cliente saco cuantas debe, cuanto suman y desde cuando
        List<MorosoResponse> morosos = new ArrayList<>();
        for (List<Factura> facturas : porCliente.values()) {
            Cliente cliente = facturas.get(0).getCliente();
            BigDecimal deuda = BigDecimal.ZERO;
            for (Factura factura : facturas) {
                if (factura.getTotal() != null) {
                    deuda = deuda.add(factura.getTotal());
                }
            }
            // como van ordenadas, la primera es la que vencio antes
            LocalDate vencidaDesde = facturas.get(0).getFechaVencimiento();
            long dias = ChronoUnit.DAYS.between(vencidaDesde, LocalDate.now());
            morosos.add(new MorosoResponse(Resumen.de(cliente), facturas.size(), deuda, vencidaDesde, dias));
        }
        return morosos;
    }

    // si no me dan las fechas las pongo yo:
    // emision = hoy, vencimiento = emision + los dias de la forma de pago del cliente
    private void completarFechas(Factura factura) {
        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDate.now());
        }
        if (factura.getFechaVencimiento() == null) {
            factura.setFechaVencimiento(factura.getFechaEmision().plusDays(diasParaPagar(factura.getCliente())));
        }
    }

    // a 30, 60 o 90 dias segun como pague el cliente. Si no, vence el mismo dia
    private int diasParaPagar(Cliente cliente) {
        if (cliente == null || cliente.getFormaPago() == null) {
            return 0;
        }
        return switch (cliente.getFormaPago()) {
            case DIAS_30 -> 30;
            case DIAS_60 -> 60;
            case DIAS_90 -> 90;
            default -> 0;
        };
    }

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
