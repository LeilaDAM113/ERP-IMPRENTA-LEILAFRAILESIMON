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
@Table(name = "albaran")
public class Albaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo ordenTrabajo;
    private String numero;
    private LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private String observaciones;
    private String estado;

}
