package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// el tiempo que un operario esta con una orden en una maquina
// lo apunta desde el movil con iniciar y finalizar
@Entity
@Table(name = "registro_tiempo")
public class RegistroTiempo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo ordenTrabajo;
    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;
    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquinaria maquina;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    // se calcula al finalizar
    private Integer minutos;
    private String observaciones;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public OrdenTrabajo getOrdenTrabajo() { return ordenTrabajo; }
    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) { this.ordenTrabajo = ordenTrabajo; }

    public Trabajador getTrabajador() { return trabajador; }
    public void setTrabajador(Trabajador trabajador) { this.trabajador = trabajador; }

    public Maquinaria getMaquina() { return maquina; }
    public void setMaquina(Maquinaria maquina) { this.maquina = maquina; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public Integer getMinutos() { return minutos; }
    public void setMinutos(Integer minutos) { this.minutos = minutos; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}