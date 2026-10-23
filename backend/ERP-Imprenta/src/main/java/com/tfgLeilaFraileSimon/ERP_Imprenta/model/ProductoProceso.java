package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// los pasos para hacer un producto y la maquina de cada paso
@Entity
@Table(name = "producto_proceso")
public class ProductoProceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Catalogo producto;
    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquinaria maquina;
    // paso 1, 2, 3...
    private Integer orden;
    // lo que tardo en preparar la maquina
    private BigDecimal horasPreparacion;
    // cuantas unidades hace la maquina en una hora
    private Integer unidadesHora;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Catalogo getProducto() { return producto; }
    public void setProducto(Catalogo producto) { this.producto = producto; }

    public Maquinaria getMaquina() { return maquina; }
    public void setMaquina(Maquinaria maquina) { this.maquina = maquina; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public BigDecimal getHorasPreparacion() { return horasPreparacion; }
    public void setHorasPreparacion(BigDecimal horasPreparacion) { this.horasPreparacion = horasPreparacion; }

    public Integer getUnidadesHora() { return unidadesHora; }
    public void setUnidadesHora(Integer unidadesHora) { this.unidadesHora = unidadesHora; }
}