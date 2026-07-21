package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

/*
 * REPOSITORIO DE TRABAJADORES.
 * Es la "puerta" para leer/escribir trabajadores en la base de datos.
 * Al extender JpaRepository ya tenemos gratis: guardar, borrar, buscar por id,
 * listar todos... sin escribir nada de SQL.
 *
 * findByEmail: Spring lee el NOMBRE del metodo y genera solo la consulta
 * "busca el trabajador cuyo email sea este". Optional = puede que exista o no.
 * Lo usamos en el login para encontrar a quien intenta entrar por su email.
 */
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
    Optional<Trabajador> findByEmail(String email);
}
