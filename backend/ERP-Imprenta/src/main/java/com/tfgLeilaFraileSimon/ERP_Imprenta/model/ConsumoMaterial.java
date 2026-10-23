package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// el material que se gasta en una orden (se resta del stock)
@Entity
@Table(name = "consumo_material")
public class ConsumoMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo ordenTrabajo;
    @ManyToOne
    @JoinColumn(name = "id_material")
    private Inventario material;
    // quien lo apunta
    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;
    private BigDecimal cantidad;
    private LocalDateTime fecha;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public OrdenTrabajo getOrdenTrabajo() { return ordenTrabajo; }
    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) { this.ordenTrabajo = ordenTrabajo; }

    public Inventario getMaterial() { return material; }
    public void setMaterial(Inventario material) { this.material = material; }

    public Trabajador getTrabajador() { return trabajador; }
    public void setTrabajador(Trabajador trabajador) { this.trabajador = trabajador; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}