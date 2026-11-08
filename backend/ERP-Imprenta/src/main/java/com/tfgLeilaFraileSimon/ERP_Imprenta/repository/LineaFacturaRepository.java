package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaFactura;

public interface LineaFacturaRepository extends JpaRepository<LineaFactura, Integer> {
    // todas las lineas de una factura (para el PDF)
    List<LineaFactura> findByFactura(Factura factura);
}
