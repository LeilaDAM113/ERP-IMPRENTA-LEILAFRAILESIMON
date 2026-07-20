package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "linea_presupuesto")
public class LineaPresupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_presupuesto")
    private Presupuesto idPresupuesto;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Catalogo idProducto;
    private Number precioUnitario;
    private Number cantidad;
    private Number subtotal;
    private Number total;
}