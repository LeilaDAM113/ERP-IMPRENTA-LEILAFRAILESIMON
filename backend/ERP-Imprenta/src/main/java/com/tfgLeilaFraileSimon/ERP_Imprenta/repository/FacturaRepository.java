package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    // facturas en alguno de esos estados que vencieron antes de una fecha,
    // de la mas antigua a la mas nueva
    List<Factura> findByEstadoInAndFechaVencimientoBeforeOrderByFechaVencimientoAsc(
            Collection<EstadoFactura> estados, LocalDate fecha);
}
