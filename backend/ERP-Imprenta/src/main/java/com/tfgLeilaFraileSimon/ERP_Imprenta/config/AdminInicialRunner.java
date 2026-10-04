package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;

/*
 * Al proteger la creacion/edicion/borrado de trabajadores con
 * @PreAuthorize("hasRole('ADMIN')") (ver TrabajadorController) nos quedamos
 * sin forma de crear el PRIMER trabajador: hace falta ser ADMIN para dar de
 * alta a alguien, pero todavia no existe ningun ADMIN. Es el clasico problema
 * del huevo y la gallina.
 *
 * Este CommandLineRunner se ejecuta solo al arrancar la aplicacion y, UNICAMENTE
 * si la tabla trabajador esta vacia, crea un admin inicial con los datos de
 * app.admin-inicial.* (application.properties, o las variables de entorno
 * ADMIN_EMAIL / ADMIN_PASSWORD). Si ya hay algun trabajador en la base de
 * datos, no hace nada.
 */
@Component
public class AdminInicialRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminInicialRunner.class);

    private final TrabajadorRepository repositorio;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin-inicial.email}")
    private String emailAdmin;

    @Value("${app.admin-inicial.password}")
    private String passwordAdmin;

    public AdminInicialRunner(TrabajadorRepository repositorio, PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return; // ya hay trabajadores dados de alta, no hace falta crear ningun admin
        }

        Trabajador admin = new Trabajador();
        admin.setNombreCompleto("Administrador");
        admin.setEmail(emailAdmin);
        admin.setPassword(passwordEncoder.encode(passwordAdmin));
        admin.setRol(RolTrabajador.ADMIN);
        admin.setActivo(true);
        admin.setFechaAlta(LocalDate.now());
        repositorio.save(admin);

        log.warn("Se ha creado un ADMIN inicial ({}) con la contrasena por defecto. "
                + "Inicia sesion y cambiala cuanto antes.", emailAdmin);
    }
}
