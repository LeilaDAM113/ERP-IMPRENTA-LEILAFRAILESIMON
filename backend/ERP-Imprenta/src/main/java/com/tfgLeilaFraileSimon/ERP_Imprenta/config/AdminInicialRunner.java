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

// crea el primer admin al arrancar si no hay ningun trabajador
// (si no nadie podria crear trabajadores, porque hay que ser admin)
// el email y la contraseña estan en application.properties
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
            return; // ya hay trabajadores, no hace falta
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
