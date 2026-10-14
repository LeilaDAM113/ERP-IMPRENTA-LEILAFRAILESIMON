package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

// Lo que devuelve la API de un trabajador.
// No lleva la contraseña ni las cosas internas de Spring (username, enabled...).
// El puesto va con su id y su nombre, o null si no tiene.
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
