package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private BigDecimal precioCompra;
    private String estado;
    private Boolean activo;
    private BigDecimal rentabilidad;
    private LocalDate ultimaRevision;
    private String observaciones;
}
