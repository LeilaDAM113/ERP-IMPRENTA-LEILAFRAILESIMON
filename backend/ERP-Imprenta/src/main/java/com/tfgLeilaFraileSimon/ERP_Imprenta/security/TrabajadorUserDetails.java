package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import java.util.List;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

public class TrabajadorUserDetails implements UserDetails {
    private final Trabajador trabajador;

    public TrabajadorUserDetails(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    @Override
    public String getUsername() {
        return trabajador.getEmail();
    }

    @Override
    public String getPassword() {
        return trabajador.getContrasena();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(trabajador.getActivo());
    }

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
