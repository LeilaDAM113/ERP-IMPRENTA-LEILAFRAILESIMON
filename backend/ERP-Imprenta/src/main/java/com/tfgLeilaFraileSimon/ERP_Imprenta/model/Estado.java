
package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Tabla catálogo (desplegable) con los valores posibles del estado de un Presupuesto.
// Ej: BORRADOR, ENVIADO, ACEPTADO, RECHAZADO.
// Tabla catálogo con los valores posibles del estado de una Factura.
// Ej: PENDIENTE, PAGADA, VENCIDA, ANULADA.
// Tabla catálogo con los valores posibles del estado de un Albarán.
// Ej: PENDIENTE, ENTREGADO, CANCELADO.
// Tabla catálogo con los valores posibles del estado de un Pago.
// Ej: PENDIENTE, COMPLETADO, FALLIDO.
// Tabla catálogo con los valores posibles del estado de una Máquina.
// Ej: OPERATIVA, EN_MANTENIMIENTO, AVERIADA, INACTIVA.
// Tabla catálogo con los valores posibles del estado de un Pedido.
// Ej: PENDIENTE, ENVIADO, RECIBIDO, CANCELADO.
// Tabla catálogo con los valores posibles del estado de una Orden de Trabajo.
// Ej: PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA.
@Entity
@Table(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String valor;
}