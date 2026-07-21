package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trabajador")
public class Trabajador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String email;
    private String contrasena;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    private BigDecimal salario;
    private LocalDate fechaAlta;
    private Boolean activo;

    // Getters SIN setters: otras clases (login/seguridad) necesitan LEER estos
    // campos (email, contrasena...), pero no dejamos ESCRIBIRLOS desde fuera;
    // de guardar datos se encarga Hibernate. Es "mirar, pero no tocar".
    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public Boolean getActivo() {
        return activo;
    }
}
