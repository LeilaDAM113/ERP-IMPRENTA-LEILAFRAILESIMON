package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

// un cliente que debe dinero: cuantas facturas tiene vencidas, cuanto debe
// y desde cuando (la factura vencida mas antigua) y cuantos dias lleva de retraso
public record MorosoResponse(
        Resumen cliente,
        int facturasVencidas,
        BigDecimal deuda,
        LocalDate vencidaDesde,
        long diasDeRetraso) {
}
