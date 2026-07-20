package com.tfgLeilaFraileSimon.ERP_Imprenta.model;


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
    private Number importe;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
    @ManyToOne
    @JoinColumn(name = "id_inventario")
    private Inventario idInventario;
    private LocalDate fechaPedido;
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor idProveedor;
    private Number cantidad;

}


