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
@Table(name = "orden_trabajo")
public class OrdenTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numero;
    private String titulo;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_presupuesto")
    private Presupuesto presupuesto;
    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String prioridad;
    private String estado;
}
