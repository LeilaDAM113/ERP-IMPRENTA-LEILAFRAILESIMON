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

// Trabajador ES ADEMAS el usuario del sistema: implementa UserDetails para que
// Spring Security pueda autenticarlo directamente (login por email + password).
@Entity
@Table(name = "trabajador")
public class Trabajador implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombreCompleto;
    private String dni;
    private String telefono;
    // unique = true evita que dos trabajadores compartan email: el login busca
    // por email esperando encontrar como mucho UNO, y si hubiera repetidos
    // fallaria de forma rara. El formato del email se valida al recibir el
    // JSON, en los DTOs (TrabajadorCrearRequest / TrabajadorActualizarRequest).
    @Column(unique = true)
    private String email;
    // Aqui se guarda SIEMPRE cifrada (BCrypt). Las reglas de entrada
    // (obligatoria al crear, opcional al actualizar, minimo 8 caracteres)
    // estan en los DTOs y en TrabajadorService. nullable = false es la ultima
    // red de seguridad: la base de datos rechaza un trabajador sin contrasena.
    // WRITE_ONLY: /api/trabajador ya devuelve TrabajadorResponse (sin
    // contrasena), pero otras entidades (OrdenTrabajo, OrdenTrabajador...)
    // siguen devolviendo el Trabajador anidado en su JSON, y ahi el hash no
    // debe salir nunca.
    @Column(nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    private BigDecimal salario;
    private LocalDate fechaAlta;
    private Boolean activo;
    // Conjunto FIJO de roles (ver RolTrabajador): antes era String libre,
    // ahora es un enum de Java mapeado como texto (EnumType.STRING), asi que
    // la columna en MySQL sigue siendo VARCHAR pero Java ya no deja guardar
    // un valor que no sea uno de los cinco roles + USER por defecto.
    // Se usa en getAuthorities() para decidir los permisos de Spring Security.
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

    // --- Metodos que exige UserDetails (Spring Security) ---

    // El "nombre de usuario" con el que se hace login -> el email
    @Override
    public String getUsername() {
        return email;
    }

    // Permisos/roles del usuario, a partir del campo "rol". Aqui NO se anade
    // el prefijo "ROLE_": la autoridad viaja tal cual el nombre de la
    // constante (ADMIN, COMERCIAL...). Por eso en los controladores se
    // comprueba con @PreAuthorize("hasAuthority('ADMIN')") y NO con hasRole(),
    // que si esperaria el prefijo "ROLE_" por delante.
    // Si el trabajador no tiene rol asignado (dato viejo, o alguien se olvido
    // de ponerlo) le damos el rol basico USER para que al menos pueda entrar,
    // aunque sin permisos de ADMIN.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        RolTrabajador rolEfectivo = rol == null ? RolTrabajador.USER : rol;
        return List.of(new SimpleGrantedAuthority(rolEfectivo.name()));
    }

    // Si el trabajador esta dado de baja (activo=false) no puede entrar
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(getActivo());
    }

    // Estas tres las pide Spring; true = "cuenta sin caducar/bloquear"
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
