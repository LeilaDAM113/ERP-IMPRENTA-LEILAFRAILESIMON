package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// datos generales para los calculos (solo hay una fila)
@Entity
@Table(name = "configuracion")
public class Configuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // IVA por defecto (21)
    private BigDecimal porcentajeIva;
    // si el producto no tiene margen uso este
    private BigDecimal margenDefecto;
    // comision de los comerciales
    private BigDecimal porcentajeComision;
    // si el trabajador no tiene coste por hora uso este
    private BigDecimal costeHoraManoObra;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public BigDecimal getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(BigDecimal porcentajeIva) { this.porcentajeIva = porcentajeIva; }

    public BigDecimal getMargenDefecto() { return margenDefecto; }
    public void setMargenDefecto(BigDecimal margenDefecto) { this.margenDefecto = margenDefecto; }

    public BigDecimal getPorcentajeComision() { return porcentajeComision; }
    public void setPorcentajeComision(BigDecimal porcentajeComision) { this.porcentajeComision = porcentajeComision; }

    public BigDecimal getCosteHoraManoObra() { return costeHoraManoObra; }
    public void setCosteHoraManoObra(BigDecimal costeHoraManoObra) { this.costeHoraManoObra = costeHoraManoObra; }
}