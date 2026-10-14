package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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

// Los trabajadores son tambien los usuarios de la aplicacion.
// Implementa UserDetails para que Spring pueda hacer el login con su email y contraseña.
@Entity
@Table(name = "trabajador")
public class Trabajador implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombreCompleto;
    private String dni;
    private String telefono;
    // el email no se puede repetir, porque es con lo que se hace login
    @Column(unique = true)
    private String email;
    // la contraseña se guarda siempre cifrada y no puede estar vacia.
    // WRITE_ONLY: se puede recibir pero nunca se devuelve en un JSON
    // (por si sale un trabajador dentro de otra cosa, por ejemplo en una orden de trabajo)
    @Column(nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    private BigDecimal salario;
    private LocalDate fechaAlta;
    private Boolean activo;
    // el rol es un enum, asi solo se pueden poner los roles que existen.
    // en la base de datos se guarda como texto (ADMIN, COMERCIAL...)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolTrabajador rol;

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

    public RolTrabajador getRol() { return rol; }
    public void setRol(RolTrabajador rol) { this.rol = rol; }

    // ---- Metodos que pide UserDetails para el login ----

    // el "usuario" para entrar es el email
    @Override
    public String getUsername() {
        return email;
    }

    // los permisos del trabajador salen de su rol (sin "ROLE_" delante).
    // si no tiene rol, le doy USER, que casi no tiene permisos
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        RolTrabajador rolEfectivo = rol == null ? RolTrabajador.USER : rol;
        return List.of(new SimpleGrantedAuthority(rolEfectivo.name()));
    }

    // si el trabajador esta dado de baja (activo = false) no puede entrar
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(getActivo());
    }

    // estas tres las pide Spring, las dejo siempre a true (cuenta valida)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
