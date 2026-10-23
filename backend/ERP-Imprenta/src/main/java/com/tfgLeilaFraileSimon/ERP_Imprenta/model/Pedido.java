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

// pedido de material a un proveedor (los materiales van en LineaPedido)
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String numero;
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
    private LocalDate fechaPedido;
    private String descripcion;
    private BigDecimal importe;
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;
    // true = lo ha hecho la app sola porque falta stock
    private Boolean automatico;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public Boolean getAutomatico() { return automatico; }
    public void setAutomatico(Boolean automatico) { this.automatico = automatico; }
}