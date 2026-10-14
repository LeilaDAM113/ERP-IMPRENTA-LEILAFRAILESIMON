package com.tfgLeilaFraileSimon.ERP_Imprenta.service;
import java.util.NoSuchElementException;

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

// Servicio de los trabajadores. Hace dos cosas:
// 1. El login: Spring lo usa para buscar al trabajador por su email.
// 2. Crear, ver, modificar y borrar trabajadores.
// Las contraseñas siempre se guardan cifradas con BCrypt.
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

    // ---- LOGIN ----
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // si ese email esta bloqueado por fallar muchas veces, no le dejo entrar
        if (loginAttemptService.estaBloqueado(email)) {
            throw new LockedException(
                    "Cuenta bloqueada temporalmente por demasiados intentos fallidos. Intentalo de nuevo en unos minutos.");
        }
        return repositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un trabajador con email " + email));
    }

    // ---- CRUD ----
    public Page<Trabajador> listar(Pageable paginacion) {
         return repositorio.findAll(paginacion);
    }
    // lo uso en /api/me para saber quien ha iniciado sesion
    public Trabajador obtenerPorEmail(String email) {
        return repositorio.findByEmail(email).orElseThrow();
    }
    public Trabajador obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // Crear un trabajador nuevo con los datos del DTO.
    // El DTO no trae id, asi que la base de datos le pone uno nuevo
    // y no se puede machacar a otro trabajador.
    // La contraseña la cifro antes de guardarla.
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

    // Modificar un trabajador: cojo el que ya esta guardado y le cambio los datos.
    // (antes, si en el JSON faltaba algun campo, se quedaba vacio sin querer)
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

        // si no me mandan rol, se queda con el que tenia
        if (datos.rol() != null) {
            existente.setRol(datos.rol());
        }

        // si no me mandan contraseña nueva, se queda la que tenia.
        // si me mandan una, la cifro y la cambio
        if (datos.password() != null && !datos.password().isBlank()) {
            existente.setPassword(passwordEncoder.encode(datos.password()));
        }

        return repositorio.save(existente);
    }

       public void eliminar(Integer id) {
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }

    // si no me dicen el rol, le pongo USER para que nunca se quede sin rol
    private RolTrabajador rolConDefecto(RolTrabajador rol) {
        return rol == null ? RolTrabajador.USER : rol;
    }

    // en el DTO solo viene el id del puesto, aqui busco el puesto de verdad.
    // sin id -> sin puesto. Si ese puesto no existe -> error 400
    private Puesto buscarPuesto(Integer idPuesto) {
        if (idPuesto == null) {
            return null;
        }
        return puestoRepositorio.findById(idPuesto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No existe un puesto con id " + idPuesto));
    }
}
