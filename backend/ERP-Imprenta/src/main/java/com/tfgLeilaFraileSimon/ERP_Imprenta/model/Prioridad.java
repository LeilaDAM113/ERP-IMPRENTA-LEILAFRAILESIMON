package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Tabla con los valores posibles de la prioridad de una Orden de Trabajo (Baja, Media,Alta,Urgente)
@Entity
@Table(name = "prioridad")
public class Prioridad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String valor;
}
