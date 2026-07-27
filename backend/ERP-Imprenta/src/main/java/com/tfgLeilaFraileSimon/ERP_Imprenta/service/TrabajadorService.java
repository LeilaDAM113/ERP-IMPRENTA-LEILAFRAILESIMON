package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;

/*
 * SERVICIO DE TRABAJADOR: hace DOS cosas.
 *  1) Login: implementa UserDetailsService (Spring lo llama al autenticar).
 *  2) CRUD: listar/obtener/guardar/actualizar/eliminar trabajadores.
 * Al crear o cambiar la contrasena, la ciframos con BCrypt antes de guardarla.
 */
@Service
public class TrabajadorService implements UserDetailsService {
    private final TrabajadorRepository repositorio;
    private final PasswordEncoder passwordEncoder;

    public TrabajadorService(TrabajadorRepository repositorio, PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    // --- LOGIN (Spring Security) ---
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un trabajador con email " + email));
    }

    // --- CRUD ---
    public List<Trabajador> listar() {
        return repositorio.findAll();
    }

    public Trabajador obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR: cifra la contrasena antes de guardar
    public Trabajador guardar(Trabajador trabajador) {
        trabajador.setPassword(passwordEncoder.encode(trabajador.getPassword()));
        return repositorio.save(trabajador);
    }

    // ACTUALIZAR: si viene contrasena nueva la cifra; si no, conserva la actual
    public Trabajador actualizar(Integer id, Trabajador trabajador) {
        Trabajador existente = repositorio.findById(id).orElseThrow();
        trabajador.setId(id);
        if (trabajador.getPassword() == null || trabajador.getPassword().isBlank()) {
            trabajador.setPassword(existente.getPassword());
        } else {
            trabajador.setPassword(passwordEncoder.encode(trabajador.getPassword()));
        }
        return repositorio.save(trabajador);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}
