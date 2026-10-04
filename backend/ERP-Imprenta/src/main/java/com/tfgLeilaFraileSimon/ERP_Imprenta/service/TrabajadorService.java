package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.security.LoginAttemptService;

/*
 * SERVICIO DE TRABAJADOR: hace DOS cosas.
 *  1) Login: implementa UserDetailsService (Spring lo llama al autenticar) y
 *     corta el paso si ese email lleva demasiados intentos fallidos seguidos.
 *  2) CRUD: listar/obtener/guardar/actualizar/eliminar trabajadores.
 * Al crear o cambiar la contrasena, la ciframos con BCrypt antes de guardarla.
 */
@Service
public class TrabajadorService implements UserDetailsService {
    private final TrabajadorRepository repositorio;
    //inyección de dependencias
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public TrabajadorService(TrabajadorRepository repositorio, PasswordEncoder passwordEncoder,
            LoginAttemptService loginAttemptService) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    // --- LOGIN (Spring Security) ---
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Antes de mirar siquiera en la base de datos comprobamos si ese email
        // esta bloqueado por demasiados fallos seguidos (fuerza bruta).
        if (loginAttemptService.estaBloqueado(email)) {
            throw new LockedException(
                    "Cuenta bloqueada temporalmente por demasiados intentos fallidos. Intentalo de nuevo en unos minutos.");
        }
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

    // AÑADIR: la contrasena es obligatoria al crear un trabajador nuevo (no
    // tiene ninguna contrasena previa que conservar) y se cifra antes de guardar.
    // Si no llega rol, se pone el rol por defecto: nunca se guarda un
    // trabajador con el rol sin asignar.
    public Trabajador guardar(Trabajador trabajador) {
        if (trabajador.getPassword() == null || trabajador.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contrasena es obligatoria al crear un trabajador");
        }
        trabajador.setPassword(passwordEncoder.encode(trabajador.getPassword()));
        trabajador.setRol(rolConDefecto(trabajador.getRol()));
        return repositorio.save(trabajador);
    }

    // ACTUALIZAR: partimos del trabajador YA guardado en la base de datos y
    // solo pisamos los campos que llegan en la peticion. Antes se guardaba el
    // objeto recibido tal cual, y si el JSON no traia algun campo (por ejemplo
    // "rol" o "activo") se quedaba a null en la base de datos sin querer.
    public Trabajador actualizar(Integer id, Trabajador datosNuevos) {
        //orElseThrow es como if else y sino lanza excep
        Trabajador existente = repositorio.findById(id).orElseThrow();

        existente.setNombreCompleto(datosNuevos.getNombreCompleto());
        existente.setDni(datosNuevos.getDni());
        existente.setTelefono(datosNuevos.getTelefono());
        existente.setEmail(datosNuevos.getEmail());
        existente.setPuesto(datosNuevos.getPuesto());
        existente.setSalario(datosNuevos.getSalario());
        existente.setFechaAlta(datosNuevos.getFechaAlta());
        existente.setActivo(datosNuevos.getActivo());

        // Si no mandan rol nuevo (no viene en el JSON), se conserva el que ya
        // tenia -> nunca se queda sin rol.
        if (datosNuevos.getRol() != null) {
            existente.setRol(datosNuevos.getRol());
        }

        // Si no mandan contrasena nueva (viene vacia o no viene), se conserva
        // la que ya tenia cifrada; si mandan una nueva, se cifra y se cambia.
        if (datosNuevos.getPassword() != null && !datosNuevos.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
        }

        return repositorio.save(existente);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }

    // Si no llega ningun rol se usa USER por defecto: el campo nunca debe
    // quedar sin valor (ver Trabajador.rol, nullable = false).
    private RolTrabajador rolConDefecto(RolTrabajador rol) {
        return rol == null ? RolTrabajador.USER : rol;
    }
}
