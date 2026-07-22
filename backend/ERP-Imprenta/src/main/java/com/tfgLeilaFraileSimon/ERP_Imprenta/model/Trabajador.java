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
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    private BigDecimal salario;
    private LocalDate fechaAlta;
    private Boolean activo;
    private String rol;

    public Integer getId() { return id; }
public void setId(Integer id) { this.id = id; }

public String getNombreCompleto() { return nombreCompleto; }
public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

public String getDni() { return dni; }
public void setDni(String dni) { this.dni = dni; }

public String getTelefono() { return telefono; }
public void setTelefono(String telefono) { this.telefono = telefono; }

public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }

public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }

public Puesto getPuesto() { return puesto; }
public void setPuesto(Puesto puesto) { this.puesto = puesto; }

public BigDecimal getSalario() { return salario; }
public void setSalario(BigDecimal salario) { this.salario = salario; }

public LocalDate getFechaAlta() { return fechaAlta; }
public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

public Boolean getActivo() { return activo; }
public void setActivo(Boolean activo) { this.activo = activo; }

public String getRol() { return rol; }
public void setRol(String rol) { this.rol = rol; }
}
