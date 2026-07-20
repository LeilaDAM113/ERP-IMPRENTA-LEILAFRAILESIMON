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
@Table(name = "inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private Number cantidad;
    private Number stockActual;
    private Number stockMinimo;
    private Number precioProducto;
    private LocalDate fechaActualizacion;
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor idProveedor;
}