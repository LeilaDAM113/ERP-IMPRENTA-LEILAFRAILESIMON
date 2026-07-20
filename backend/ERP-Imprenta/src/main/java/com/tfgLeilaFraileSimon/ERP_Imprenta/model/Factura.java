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
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numero;
    @ManyToOne
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo idOrdenTrabajo;
    @ManyToOne
    @JoinColumn(name = "id_presupuesto")
    private Presupuesto idPresupuesto;
    @ManyToOne
    @JoinColumn(name = "id_albaran")
    private Albaran idAlbaran;
    private LocalDate fechaEmision;
    private LocalDate fechaPago;
    private String metodoPago;
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    private Number total;
}