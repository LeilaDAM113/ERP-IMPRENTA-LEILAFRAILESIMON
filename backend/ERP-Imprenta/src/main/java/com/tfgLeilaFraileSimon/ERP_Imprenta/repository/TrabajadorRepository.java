package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

// con JpaRepository ya tengo guardar, borrar, buscar... sin hacer SQL
// findByEmail lo hace spring solo por el nombre del metodo (lo uso en el login)
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
    Optional<Trabajador> findByEmail(String email);
}
