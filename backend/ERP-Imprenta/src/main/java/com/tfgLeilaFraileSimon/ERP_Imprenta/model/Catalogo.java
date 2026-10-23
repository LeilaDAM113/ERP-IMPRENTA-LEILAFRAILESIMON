package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// los productos que vendo
@Entity
@Table(name = "catalogo")
public class Catalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    // % de beneficio que le sumo al coste
    private BigDecimal margen;
    // lo minimo que cobro, para no perder dinero
    private BigDecimal importeMinimo;
    // false = descatalogado
    private Boolean activo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getMargen() { return margen; }
    public void setMargen(BigDecimal margen) { this.margen = margen; }

    public BigDecimal getImporteMinimo() { return importeMinimo; }
    public void setImporteMinimo(BigDecimal importeMinimo) { this.importeMinimo = importeMinimo; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}