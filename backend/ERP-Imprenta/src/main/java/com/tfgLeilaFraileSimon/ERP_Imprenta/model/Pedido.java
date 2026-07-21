package com.tfgLeilaFraileSimon.ERP_Imprenta.model;


import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal importe;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    @ManyToOne
    @JoinColumn(name = "id_inventario")
    private Inventario inventario;
    private LocalDate fechaPedido;
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
    private Integer cantidad;

}
