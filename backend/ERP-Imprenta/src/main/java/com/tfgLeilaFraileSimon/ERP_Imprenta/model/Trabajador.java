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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
    // fallaria de forma rara. @NotBlank/@Email comprueban el formato al recibir
    // el JSON (necesita @Valid en el controlador para activarse).
    @Column(unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    private String email;
    // WRITE_ONLY: se puede ENVIAR (al crear/actualizar) pero NUNCA se devuelve en las respuestas JSON.
    // Distincion importante:
    //  - En la PETICION (JSON) SI puede venir vacia al actualizar -> se
    //    interpreta como "no cambies la contrasena" en TrabajadorService.
    //    Por eso @Size no lleva @NotBlank: admite null en el JSON de entrada.
    //  - En la BASE DE DATOS nunca debe quedar guardada como null. Eso lo
    //    garantiza TrabajadorService (obliga a mandarla al crear, y al
    //    actualizar conserva la que ya habia si no llega una nueva) y,
    //    como ultima red de seguridad, nullable = false: si por lo que sea
    //    se intentara guardar sin contrasena, la base de datos lo rechaza.
    @Column(nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    private BigDecimal salario;
    private LocalDate fechaAlta;
    private Boolean activo;
    // Texto libre (ADMIN, COMERCIAL, ENCARGADO_TALLER, OPERARIO, TRANSPORTISTA...),
    // siguiendo la norma del proyecto de no usar tablas de catalogo para esto.
    // Se usa en getAuthorities() para decidir los permisos de Spring Security.
    // Igual que la contrasena, nunca debe quedar null en la base de datos:
    // TrabajadorService le pone "USER" por defecto si no llega ninguno, y
    // nullable = false es la red de seguridad por si algo se saltara esa logica.
    @Column(nullable = false)
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

    // --- Metodos que exige UserDetails (Spring Security) ---

    // El "nombre de usuario" con el que se hace login -> el email
    @Override
    public String getUsername() {
        return email;
    }

    // Permisos/roles del usuario, a partir del campo "rol" (texto libre).
    // Aqui NO se anade el prefijo "ROLE_": la autoridad viaja tal cual esta en
    // la base de datos (ADMIN, COMERCIAL...). Por eso en los controladores se
    // comprueba con @PreAuthorize("hasAuthority('ADMIN')") y NO con hasRole(),
    // que si esperaria el prefijo "ROLE_" por delante.
    // Si el trabajador no tiene rol asignado (dato viejo, o alguien se olvido
    // de ponerlo) le damos el rol basico "USER" para que al menos pueda
    // entrar, aunque sin permisos de ADMIN.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rolEfectivo = (rol == null || rol.isBlank()) ? "USER" : rol.trim().toUpperCase();
        return List.of(new SimpleGrantedAuthority(rolEfectivo));
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
