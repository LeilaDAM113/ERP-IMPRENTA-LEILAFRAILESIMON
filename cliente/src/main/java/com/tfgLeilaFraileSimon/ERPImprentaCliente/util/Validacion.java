package com.tfgLeilaFraileSimon.ERPImprentaCliente.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.CampoConfig;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo;

/** Equivalente a frontend/js/validacion.js: comprueba los errores obvios ANTES
 *  de llamar a la API (campo obligatorio vacio, email mal escrito, numero no
 *  numerico...). Esto no sustituye la validacion del backend, solo evita
 *  peticiones que ya sabemos que van a fallar. */
public final class Validacion {

    private static final Pattern EXPRESION_EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private Validacion() {
    }

    public static boolean esEmailValido(String valor) {
        return valor != null && EXPRESION_EMAIL.matcher(valor.trim()).matches();
    }

    /** Devuelve el mensaje de error del campo, o null si es valido. */
    public static String validarCampoGenerico(CampoConfig campo, String valorCrudo) {
        if (campo.tipo == TipoCampo.BOOLEANO) {
            return null; // un checkbox nunca esta "vacio"
        }

        boolean esVacio = valorCrudo == null || valorCrudo.isBlank();

        if (campo.requerido && esVacio) {
            return campo.etiqueta + " es obligatorio.";
        }
        if (esVacio) {
            return null; // campo opcional y vacio: no hay nada que comprobar
        }

        if (campo.tipo == TipoCampo.NUMERO || campo.tipo == TipoCampo.DECIMAL) {
            try {
                double numero = Double.parseDouble(valorCrudo.trim().replace(',', '.'));
                if (campo.tipo == TipoCampo.NUMERO && numero != Math.floor(numero)) {
                    return campo.etiqueta + " debe ser un número entero.";
                }
            } catch (NumberFormatException e) {
                return campo.etiqueta + " debe ser un número.";
            }
        }

        // El navegador valida el formato de <input type=date> gratis; aqui el
        // campo es texto libre, asi que hay que comprobarlo a mano.
        if (campo.tipo == TipoCampo.FECHA) {
            try {
                LocalDate.parse(valorCrudo.trim());
            } catch (DateTimeParseException e) {
                return campo.etiqueta + " debe tener formato aaaa-mm-dd.";
            }
        }

        if (campo.tipo == TipoCampo.TEXTO && campo.nombre.toLowerCase().contains("email") && !esEmailValido(valorCrudo)) {
            return campo.etiqueta + " no tiene formato de email válido.";
        }

        return null;
    }
}
