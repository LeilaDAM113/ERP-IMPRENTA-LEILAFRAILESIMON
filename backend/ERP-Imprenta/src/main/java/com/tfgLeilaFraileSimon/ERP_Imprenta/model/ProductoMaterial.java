package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// el material que gasta un producto
@Entity
@Table(name = "producto_material")
public class ProductoMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Catalogo producto;
    @ManyToOne
    @JoinColumn(name = "id_material")
    private Inventario material;
    // cuanto material gasta cada unidad
    private BigDecimal cantidadPorUnidad;
    // lo que se gasta siempre
    private BigDecimal cantidadFija;
    // % que se desperdicia
    private BigDecimal merma;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Catalogo getProducto() { return producto; }
    public void setProducto(Catalogo producto) { this.producto = producto; }

    public Inventario getMaterial() { return material; }
    public void setMaterial(Inventario material) { this.material = material; }

    public BigDecimal getCantidadPorUnidad() { return cantidadPorUnidad; }
    public void setCantidadPorUnidad(BigDecimal cantidadPorUnidad) { this.cantidadPorUnidad = cantidadPorUnidad; }

    public BigDecimal getCantidadFija() { return cantidadFija; }
    public void setCantidadFija(BigDecimal cantidadFija) { this.cantidadFija = cantidadFija; }

    public BigDecimal getMerma() { return merma; }
    public void setMerma(BigDecimal merma) { this.merma = merma; }
}