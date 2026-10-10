package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorActualizarRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorCrearRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PuestoRepository;
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
    private final PuestoRepository puestoRepositorio;
    //inyección de dependencias
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public TrabajadorService(TrabajadorRepository repositorio, PuestoRepository puestoRepositorio,
            PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.repositorio = repositorio;
        this.puestoRepositorio = puestoRepositorio;
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
    public Page<Trabajador> listar(Pageable paginacion) {
         return repositorio.findAll(paginacion);
    }
    // Se usa en GET (/api/me)
    public Trabajador obtenerPorEmail(String email) {
        return repositorio.findByEmail(email).orElseThrow();
    }
    public Trabajador obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // AÑADIR: se construye un Trabajador NUEVO a partir del DTO. Como el DTO
    // no tiene id, el trabajador siempre llega a save() con id null y la base
    // de datos le asigna uno: es imposible pisar a otro trabajador existente.
    // La contrasena ya viene validada como obligatoria (@NotBlank en el DTO)
    // y aqui se cifra antes de guardar. Si no llega rol, se pone el rol por
    // defecto: nunca se guarda un trabajador con el rol sin asignar.
    public Trabajador guardar(TrabajadorCrearRequest datos) {
        Trabajador trabajador = new Trabajador();
        trabajador.setNombreCompleto(datos.nombreCompleto());
        trabajador.setDni(datos.dni());
        trabajador.setTelefono(datos.telefono());
        trabajador.setEmail(datos.email());
        trabajador.setPassword(passwordEncoder.encode(datos.password()));
        trabajador.setPuesto(buscarPuesto(datos.idPuesto()));
        trabajador.setSalario(datos.salario());
        trabajador.setFechaAlta(datos.fechaAlta());
        trabajador.setActivo(datos.activo());
        trabajador.setRol(rolConDefecto(datos.rol()));
        return repositorio.save(trabajador);
    }

    // ACTUALIZAR: partimos del trabajador YA guardado en la base de datos y
    // solo pisamos los campos que llegan en la peticion. Antes se guardaba el
    // objeto recibido tal cual, y si el JSON no traia algun campo (por ejemplo
    // "rol" o "activo") se quedaba a null en la base de datos sin querer.
    public Trabajador actualizar(Integer id, TrabajadorActualizarRequest datos) {
        //orElseThrow es como if else y sino lanza excep
        Trabajador existente = repositorio.findById(id).orElseThrow();

        existente.setNombreCompleto(datos.nombreCompleto());
        existente.setDni(datos.dni());
        existente.setTelefono(datos.telefono());
        existente.setEmail(datos.email());
        existente.setPuesto(buscarPuesto(datos.idPuesto()));
        existente.setSalario(datos.salario());
        existente.setFechaAlta(datos.fechaAlta());
        existente.setActivo(datos.activo());

        // Si no mandan rol nuevo (no viene en el JSON), se conserva el que ya
        // tenia -> nunca se queda sin rol.
        if (datos.rol() != null) {
            existente.setRol(datos.rol());
        }

        // Si no mandan contrasena nueva (viene vacia o no viene), se conserva
        // la que ya tenia cifrada; si mandan una nueva, se cifra y se cambia.
        if (datos.password() != null && !datos.password().isBlank()) {
            existente.setPassword(passwordEncoder.encode(datos.password()));
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

    // El DTO trae solo el id del puesto: aqui se busca el Puesto de verdad.
    // Sin id -> trabajador sin puesto. Id que no existe -> error 400.
    private Puesto buscarPuesto(Integer idPuesto) {
        if (idPuesto == null) {
            return null;
        }
        return puestoRepositorio.findById(idPuesto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No existe un puesto con id " + idPuesto));
    }
}
