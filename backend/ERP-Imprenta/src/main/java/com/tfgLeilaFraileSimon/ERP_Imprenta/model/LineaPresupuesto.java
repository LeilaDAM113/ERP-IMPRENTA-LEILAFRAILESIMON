package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "linea_presupuesto")
public class LineaPresupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_presupuesto")
    private Presupuesto presupuesto;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Catalogo producto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
    private BigDecimal total;
    // de donde sale el precio
    private BigDecimal costeMaterial;
    private BigDecimal costeMaquina;
    private BigDecimal costeManoObra;
    // horas previstas, para compararlas con las reales
    private BigDecimal horasEstimadas;
    private BigDecimal margen;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Presupuesto getPresupuesto() { return presupuesto; }
    public void setPresupuesto(Presupuesto presupuesto) { this.presupuesto = presupuesto; }

    public Catalogo getProducto() { return producto; }
    public void setProducto(Catalogo producto) { this.producto = producto; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public BigDecimal getCosteMaterial() { return costeMaterial; }
    public void setCosteMaterial(BigDecimal costeMaterial) { this.costeMaterial = costeMaterial; }

    public BigDecimal getCosteMaquina() { return costeMaquina; }
    public void setCosteMaquina(BigDecimal costeMaquina) { this.costeMaquina = costeMaquina; }

    public BigDecimal getCosteManoObra() { return costeManoObra; }
    public void setCosteManoObra(BigDecimal costeManoObra) { this.costeManoObra = costeManoObra; }

    public BigDecimal getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; }

    public BigDecimal getMargen() { return margen; }
    public void setMargen(BigDecimal margen) { this.margen = margen; }
}