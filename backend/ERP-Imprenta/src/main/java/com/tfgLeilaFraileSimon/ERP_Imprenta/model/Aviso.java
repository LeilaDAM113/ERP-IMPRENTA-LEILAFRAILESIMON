package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

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

// avisos para un trabajador (orden terminada, falta material...)
@Entity
@Table(name = "aviso")
public class Aviso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // a quien le llega
    @ManyToOne
    @JoinColumn(name = "id_destinatario")
    private Trabajador destinatario;
    @Enumerated(EnumType.STRING)
    private TipoAviso tipo;
    private String mensaje;
    private LocalDateTime fecha;
    private Boolean leido;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Trabajador getDestinatario() { return destinatario; }
    public void setDestinatario(Trabajador destinatario) { this.destinatario = destinatario; }

    public TipoAviso getTipo() { return tipo; }
    public void setTipo(TipoAviso tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
}