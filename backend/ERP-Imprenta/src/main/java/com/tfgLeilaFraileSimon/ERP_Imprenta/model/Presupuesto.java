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
@Table(name = "presupuesto")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;
    private LocalDate fechaEmision;
    private LocalDate fechaValidez;
    private String descripcion;
    private Number importe;
    private Number subtotal;
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
}