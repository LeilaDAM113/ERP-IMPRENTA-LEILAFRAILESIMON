package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Column(unique = true)
    private String numero;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    // (el comercial para las comisiones)
    @ManyToOne
    @JoinColumn(name = "id_comercial")
    private Trabajador comercial;
    private LocalDate fechaEmision;
    // cuando se lo mando al cliente
    private LocalDate fechaEnvio;
    private LocalDate fechaValidez;
    private String descripcion;
    private BigDecimal importe;
    private BigDecimal subtotal;
    // el IVA (21)
    private BigDecimal porcentajeIva;
    @Enumerated(EnumType.STRING)
    private EstadoPresupuesto estado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Trabajador getComercial() { return comercial; }
    public void setComercial(Trabajador comercial) { this.comercial = comercial; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDate fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDate getFechaValidez() { return fechaValidez; }
    public void setFechaValidez(LocalDate fechaValidez) { this.fechaValidez = fechaValidez; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(BigDecimal porcentajeIva) { this.porcentajeIva = porcentajeIva; }

    public EstadoPresupuesto getEstado() { return estado; }
    public void setEstado(EstadoPresupuesto estado) { this.estado = estado; }
}