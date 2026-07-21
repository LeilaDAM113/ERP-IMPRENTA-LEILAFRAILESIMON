package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    private String cif;
    private String nombreComercial;
    private String razonSocial;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private String telefono;
    private String email;
    private String web;
}
