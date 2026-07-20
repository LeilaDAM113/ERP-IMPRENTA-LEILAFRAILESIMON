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
@Table(name = "maquinaria")
public class Maquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String tipo;
    private LocalDate fechaCompra;
    private Number precioCompra;
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    private Boolean activo;
    private Number rentabilidad;
    private LocalDate ultimaRevision;
    private String observaciones;
}