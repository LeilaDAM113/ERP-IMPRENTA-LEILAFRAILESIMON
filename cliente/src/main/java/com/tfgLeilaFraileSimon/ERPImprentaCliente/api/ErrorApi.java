package com.tfgLeilaFraileSimon.ERPImprentaCliente.api;

/** Equivalente a la clase ErrorApi de frontend/js/api.js: permite distinguir
 *  "la API respondio con un fallo" (con su status) de un simple error de red. */
public class ErrorApi extends Exception {

    private final int status;

    public ErrorApi(int status, String mensaje) {
        super(mensaje);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
