package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

// Repositorio de trabajadores: es lo que lee y guarda en la base de datos.
// Con JpaRepository ya tengo guardar, borrar, buscar por id, listar... sin SQL.
// findByEmail: Spring entiende el nombre del metodo y hace la consulta solo.
// Lo uso en el login para buscar al trabajador por su email.
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
    Optional<Trabajador> findByEmail(String email);
}
