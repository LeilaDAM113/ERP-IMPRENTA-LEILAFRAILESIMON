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

// los trabajadores son tambien los usuarios que entran en la app
// implementa UserDetails para que spring pueda hacer el login
@Entity
@Table(name = "trabajador")
public class Trabajador implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombreCompleto;
    private String dni;
    private String telefono;
    // el email no se puede repetir porque es con lo que se entra
    @Column(unique = true)
    private String email;
    // la contraseña va cifrada y es obligatoria
    // WRITE_ONLY: se puede mandar pero nunca sale en el json
    @Column(nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    private BigDecimal salario;
    private LocalDate fechaAlta;
    private Boolean activo;
    // el rol es un enum y se guarda como texto (ADMIN, COMERCIAL...)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolTrabajador rol;
    // lo que cuesta una hora de este trabajador (para los presupuestos)
    private BigDecimal costeHora;

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

    public BigDecimal getCosteHora() { return costeHora; }
    public void setCosteHora(BigDecimal costeHora) { this.costeHora = costeHora; }

    // ---- metodos de UserDetails para el login ----

    // para entrar se usa el email
    @Override
    public String getUsername() {
        return email;
    }

    // los permisos salen del rol, si no tiene le pongo USER
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        RolTrabajador rolEfectivo = rol == null ? RolTrabajador.USER : rol;
        return List.of(new SimpleGrantedAuthority(rolEfectivo.name()));
    }

    // si esta dado de baja no puede entrar
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(getActivo());
    }

    // estas tres las pide spring, las dejo a true
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
