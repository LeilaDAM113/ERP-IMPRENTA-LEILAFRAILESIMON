package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_factura")
    private Factura idFactura;
    private String descripcion;
    private LocalDate fechaEmision;
    private LocalDate fechaPago;
    private String metodoPago;
    private Number importe;
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    @ManyToOne
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo idOrdenTrabajo;
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor idProveedor;
    @ManyToOne
    @JoinColumn(name = "id_inventario")
    private Inventario idInventario;
}