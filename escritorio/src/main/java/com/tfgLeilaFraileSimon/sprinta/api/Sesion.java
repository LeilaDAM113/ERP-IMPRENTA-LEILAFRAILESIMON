package com.tfgLeilaFraileSimon.sprinta.api;

import com.tfgLeilaFraileSimon.sprinta.modelo.Trabajador;

// quien ha entrado: el token del login y sus datos.
// es static porque solo hay una sesion en toda la app
public class Sesion {
    private static String token;
    private static Trabajador trabajador;

    public static void iniciar(String nuevoToken, Trabajador quien) {
        token = nuevoToken;
        trabajador = quien;
    }

    public static void cerrar() {
        token = null;
        trabajador = null;
    }

    public static String getToken() {
        return token;
    }

    public static Trabajador getTrabajador() {
        return trabajador;
    }
}
