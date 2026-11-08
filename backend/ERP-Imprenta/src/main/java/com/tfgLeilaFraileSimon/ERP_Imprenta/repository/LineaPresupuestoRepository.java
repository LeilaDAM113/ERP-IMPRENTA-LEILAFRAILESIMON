package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;

public interface LineaPresupuestoRepository extends JpaRepository<LineaPresupuesto, Integer> {
    // todas las lineas de un presupuesto (para sumar su total)
    List<LineaPresupuesto> findByPresupuesto(Presupuesto presupuesto);
}
