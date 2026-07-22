package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import java.util.List;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

/*
 * "TRADUCTOR" ENTRE NUESTRO Trabajador Y SPRING SECURITY.
 * Spring Security no sabe lo que es un "Trabajador"; solo entiende su propio
 * formato llamado UserDetails (usuario, contrasena, si esta activo, permisos...).
 * Esta clase envuelve a un Trabajador y responde a esas preguntas de Spring,
 * asi no tenemos que ensuciar la entidad Trabajador con codigo del framework.
 */
public class TrabajadorUserDetails implements UserDetails {
    private final Trabajador trabajador;

    public TrabajadorUserDetails(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    // Spring pregunta "cual es el nombre de usuario" -> usamos el email
    @Override
    public String getUsername() {
        return trabajador.getEmail();
    }

    // Spring pregunta "cual es la contrasena guardada" (ya cifrada) para compararla
    @Override
    public String getPassword() {
        return trabajador.getPassword();
    }

    // Permisos/roles del usuario. De momento todos tienen el mismo (ROLE_USER)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Si el trabajador esta dado de baja (activo=false) no puede entrar
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(trabajador.getActivo());
    }

    // Estas cuatro las pide Spring; devolvemos true = "cuenta sin caducar/bloquear"
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
