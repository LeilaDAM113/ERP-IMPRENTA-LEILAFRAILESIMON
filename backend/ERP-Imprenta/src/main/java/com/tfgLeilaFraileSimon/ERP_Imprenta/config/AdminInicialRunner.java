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

// Crea el primer administrador al arrancar la aplicacion.
// Hace falta porque para dar de alta trabajadores hay que ser ADMIN,
// y la primera vez todavia no hay ninguno.
// Solo lo crea si la tabla de trabajadores esta vacia.
// Su email y contraseña estan en application.properties (app.admin-inicial).
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
