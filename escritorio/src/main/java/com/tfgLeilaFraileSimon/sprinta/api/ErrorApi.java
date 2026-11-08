package com.tfgLeilaFraileSimon.sprinta.api;

// un error que me devuelve la API (401, 403, 404...) con su mensaje para enseñarlo en pantalla
public class ErrorApi extends Exception {
    private final int estado;

    public ErrorApi(int estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }
}
