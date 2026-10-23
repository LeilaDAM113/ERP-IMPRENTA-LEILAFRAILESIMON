package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "albaran")
public class Albaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo ordenTrabajo;
    @ManyToOne
    @JoinColumn(name = "id_transportista")
    private Trabajador transportista;
    private String numero;
    private LocalDate fechaEmision;
    private LocalDateTime fechaEntrega;
    private String firma;
    private String nombreReceptor;
    private String observaciones;
    @Enumerated(EnumType.STRING)
    private EstadoAlbaran estado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public OrdenTrabajo getOrdenTrabajo() { return ordenTrabajo; }
    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) { this.ordenTrabajo = ordenTrabajo; }

    public Trabajador getTransportista() { return transportista; }
    public void setTransportista(Trabajador transportista) { this.transportista = transportista; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getFirma() { return firma; }
    public void setFirma(String firma) { this.firma = firma; }

    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public EstadoAlbaran getEstado() { return estado; }
    public void setEstado(EstadoAlbaran estado) { this.estado = estado; }
}