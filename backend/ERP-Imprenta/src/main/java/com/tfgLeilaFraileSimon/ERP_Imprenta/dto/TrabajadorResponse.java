package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

/*
 * Lo que la API DEVUELVE de un trabajador.
 *
 * Solo lleva los datos que interesan al cliente: ni la contrasena, ni los
 * campos que Trabajador tiene por implementar UserDetails (username,
 * authorities, enabled, accountNonExpired...), que son cosa interna de
 * Spring Security y antes se colaban en el JSON.
 *
 * El puesto se devuelve como objeto {id, nombre} para poder mostrar el nombre
 * sin otra peticion. Es null si el trabajador no tiene puesto asignado.
 */
public record TrabajadorResponse(
        Integer id,
        String nombreCompleto,
        String dni,
        String telefono,
        String email,
        PuestoResumen puesto,
        BigDecimal salario,
        LocalDate fechaAlta,
        Boolean activo,
        RolTrabajador rol) {

    public record PuestoResumen(Integer id, String nombre) {
    }

    public static TrabajadorResponse desde(Trabajador t) {
        Puesto p = t.getPuesto();
        return new TrabajadorResponse(
                t.getId(),
                t.getNombreCompleto(),
                t.getDni(),
                t.getTelefono(),
                t.getEmail(),
                p == null ? null : new PuestoResumen(p.getId(), p.getNombre()),
                t.getSalario(),
                t.getFechaAlta(),
                t.getActivo(),
                t.getRol());
    }
}
