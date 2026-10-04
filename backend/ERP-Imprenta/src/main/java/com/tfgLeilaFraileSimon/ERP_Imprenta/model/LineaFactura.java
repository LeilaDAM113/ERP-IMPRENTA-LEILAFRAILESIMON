package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "linea_factura")
public class LineaFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_factura")
    private Factura factura;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Catalogo producto;
    @ManyToOne
    @JoinColumn(name = "id_linea_albaran")
    private LineaAlbaran lineaAlbaran;
    @ManyToOne
    @JoinColumn(name = "id_linea_presupuesto")
    private LineaPresupuesto lineaPresupuesto;
    private Integer cantidad;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public Catalogo getProducto() { return producto; }
    public void setProducto(Catalogo producto) { this.producto = producto; }

    public LineaAlbaran getLineaAlbaran() { return lineaAlbaran; }
    public void setLineaAlbaran(LineaAlbaran lineaAlbaran) { this.lineaAlbaran = lineaAlbaran; }

    public LineaPresupuesto getLineaPresupuesto() { return lineaPresupuesto; }
    public void setLineaPresupuesto(LineaPresupuesto lineaPresupuesto) { this.lineaPresupuesto = lineaPresupuesto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

}
