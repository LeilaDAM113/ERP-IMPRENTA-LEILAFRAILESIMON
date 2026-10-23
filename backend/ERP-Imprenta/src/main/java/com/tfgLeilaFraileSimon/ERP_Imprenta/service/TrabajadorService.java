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
import com.tfgLeilaFraileSimon.ERP_Imprenta.auth.LoginAttemptService;

// servicio de trabajadores: el login (buscar por email) y crear, ver, modificar y borrar
// las contraseñas siempre cifradas con BCrypt
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
        // si esta bloqueado por fallar mucho no le dejo entrar
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
    // para /api/me
    public Trabajador obtenerPorEmail(String email) {
        return repositorio.findByEmail(email).orElseThrow();
    }
    public Trabajador obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // crear un trabajador con los datos del DTO (el id lo pone la base de datos)
    // la contraseña la cifro antes de guardarla
    public Trabajador guardar(TrabajadorCrearRequest datos) {
        Trabajador trabajador = new Trabajador();
        trabajador.setNombreCompleto(datos.nombreCompleto());
        trabajador.setDni(datos.dni());
        trabajador.setTelefono(datos.telefono());
        trabajador.setEmail(datos.email());
        trabajador.setPassword(passwordEncoder.encode(datos.password()));
        trabajador.setPuesto(buscarPuesto(datos.idPuesto()));
        trabajador.setSalario(datos.salario());
        trabajador.setCosteHora(datos.costeHora());
        trabajador.setFechaAlta(datos.fechaAlta());
        trabajador.setActivo(datos.activo());
        trabajador.setRol(rolConDefecto(datos.rol()));
        return repositorio.save(trabajador);
    }

    // modificar: cojo el que ya hay y le cambio los datos
    public Trabajador actualizar(Integer id, TrabajadorActualizarRequest datos) {
        //orElseThrow es como if else y sino lanza excep
        Trabajador existente = repositorio.findById(id).orElseThrow();

        existente.setNombreCompleto(datos.nombreCompleto());
        existente.setDni(datos.dni());
        existente.setTelefono(datos.telefono());
        existente.setEmail(datos.email());
        existente.setPuesto(buscarPuesto(datos.idPuesto()));
        existente.setSalario(datos.salario());
        existente.setCosteHora(datos.costeHora());
        existente.setFechaAlta(datos.fechaAlta());
        existente.setActivo(datos.activo());

        // si no mandan rol se queda el que tenia
        if (datos.rol() != null) {
            existente.setRol(datos.rol());
        }

        // si no mandan contraseña se queda la de antes, si mandan una la cifro
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

    // si no hay rol le pongo USER
    private RolTrabajador rolConDefecto(RolTrabajador rol) {
        return rol == null ? RolTrabajador.USER : rol;
    }

    // del DTO solo viene el id del puesto, aqui lo busco (si no existe -> 400)
    private Puesto buscarPuesto(Integer idPuesto) {
        if (idPuesto == null) {
            return null;
        }
        return puestoRepositorio.findById(idPuesto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No existe un puesto con id " + idPuesto));
    }
}
