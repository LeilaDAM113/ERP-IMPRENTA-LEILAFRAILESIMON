package com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo;

import java.util.List;

/** Equivalente a cada entrada del objeto ENTIDADES de frontend/js/crud.js. */
public class EntidadConfig {

    public final String clave;
    public final String titulo;
    public final String subtitulo;
    public final String endpoint;
    public final List<CampoConfig> campos;

    public EntidadConfig(String clave, String titulo, String subtitulo, String endpoint, List<CampoConfig> campos) {
        this.clave = clave;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.endpoint = endpoint;
        this.campos = campos;
    }
}
