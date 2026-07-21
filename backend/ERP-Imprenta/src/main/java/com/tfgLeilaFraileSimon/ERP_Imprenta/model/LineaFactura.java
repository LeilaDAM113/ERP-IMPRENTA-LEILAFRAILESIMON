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
}
