package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;

// motor de calculo: aqui se calculan los precios y el IVA (asi esta todo en un sitio)
@Service
public class CalculoService {
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final BigDecimal IVA_POR_DEFECTO = BigDecimal.valueOf(21);

    // precio de una linea de presupuesto:
    // coste = material + maquina + mano de obra
    // total = coste + el margen (%), y nunca menos que el importe minimo del producto
    // precio por unidad = total / cantidad
    public void calcularLinea(LineaPresupuesto linea) {
        BigDecimal coste = cero(linea.getCosteMaterial())
                .add(cero(linea.getCosteMaquina()))
                .add(cero(linea.getCosteManoObra()));
        BigDecimal cantidad = BigDecimal.valueOf(linea.getCantidad());
        Catalogo producto = linea.getProducto();
        BigDecimal total;

        if (coste.signum() > 0) {
            // si no me dicen el margen, uso el del producto
            if (linea.getMargen() == null && producto != null) {
                linea.setMargen(producto.getMargen());
            }
            linea.setSubtotal(dinero(coste));
            total = coste.add(porcentaje(coste, cero(linea.getMargen())));
        } else {
            // sin costes: el precio por unidad lo pone el comercial a mano
            total = cero(linea.getPrecioUnitario()).multiply(cantidad);
            linea.setSubtotal(dinero(total));
        }

        // nunca menos que el importe minimo del producto
        if (producto != null && producto.getImporteMinimo() != null
                && total.compareTo(producto.getImporteMinimo()) < 0) {
            total = producto.getImporteMinimo();
        }
        linea.setTotal(dinero(total));
        linea.setPrecioUnitario(total.divide(cantidad, 4, RoundingMode.HALF_UP));
    }

    // totales del presupuesto: subtotal = suma de las lineas, importe = subtotal + IVA
    public void calcularPresupuesto(Presupuesto presupuesto, List<LineaPresupuesto> lineas) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (LineaPresupuesto linea : lineas) {
            subtotal = subtotal.add(cero(linea.getTotal()));
        }
        presupuesto.setSubtotal(dinero(subtotal));
        aplicarIva(presupuesto);
    }

    // importe con IVA del presupuesto (si no tiene IVA le pongo el 21%)
    public void aplicarIva(Presupuesto presupuesto) {
        if (presupuesto.getPorcentajeIva() == null) {
            presupuesto.setPorcentajeIva(IVA_POR_DEFECTO);
        }
        BigDecimal subtotal = cero(presupuesto.getSubtotal());
        presupuesto.setSubtotal(dinero(subtotal));
        presupuesto.setImporte(dinero(subtotal.add(porcentaje(subtotal, presupuesto.getPorcentajeIva()))));
    }

    // factura: cuota de IVA = base * IVA / 100 y total = base + cuota
    public void calcularFactura(Factura factura) {
        if (factura.getPorcentajeIva() == null) {
            factura.setPorcentajeIva(IVA_POR_DEFECTO);
        }
        BigDecimal base = cero(factura.getBaseImponible());
        BigDecimal cuota = porcentaje(base, factura.getPorcentajeIva());
        factura.setBaseImponible(dinero(base));
        factura.setCuotaIva(dinero(cuota));
        factura.setTotal(dinero(base.add(cuota)));
    }

    // el tanto por ciento de una cantidad (por ejemplo el 21% de 100 = 21)
    private BigDecimal porcentaje(BigDecimal cantidad, BigDecimal porcentaje) {
        return cantidad.multiply(porcentaje).divide(CIEN, 4, RoundingMode.HALF_UP);
    }

    // el dinero siempre con 2 decimales y redondeado (130,559 -> 130,56)
    private BigDecimal dinero(BigDecimal cantidad) {
        return cantidad.setScale(2, RoundingMode.HALF_UP);
    }

    // si no hay nada (null) cuento 0
    private BigDecimal cero(BigDecimal cantidad) {
        return cantidad == null ? BigDecimal.ZERO : cantidad;
    }
}
