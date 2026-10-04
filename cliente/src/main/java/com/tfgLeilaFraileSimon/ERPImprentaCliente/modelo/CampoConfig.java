package com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo;

/** Equivalente a cada entrada del array "campos" de una entidad en frontend/js/crud.js. */
public class CampoConfig {

    public final String nombre;
    public final String etiqueta;
    public final TipoCampo tipo;
    public boolean requerido;
    public boolean anchoCompleto;
    public String entidadRelacion; // solo para tipo == RELACION
    public String[] opciones; // solo para tipo == SELECT

    public CampoConfig(String nombre, String etiqueta, TipoCampo tipo) {
        this.nombre = nombre;
        this.etiqueta = etiqueta;
        this.tipo = tipo;
    }

    public CampoConfig requerido() {
        this.requerido = true;
        return this;
    }

    public CampoConfig anchoCompleto() {
        this.anchoCompleto = true;
        return this;
    }

    public CampoConfig relacion(String entidad) {
        this.entidadRelacion = entidad;
        return this;
    }

    public CampoConfig opciones(String... opciones) {
        this.opciones = opciones;
        return this;
    }
}
