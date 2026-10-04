package com.tfgLeilaFraileSimon.ERPImprentaCliente.util;

import com.fasterxml.jackson.databind.JsonNode;

/** Equivalente a etiquetaDeRegistro() de frontend/js/crud.js. */
public final class Etiquetas {

    private static final String[] CAMPOS_ETIQUETA = {"numero", "nombreCompleto", "nombreComercial", "nombre", "descripcion"};

    private Etiquetas() {
    }

    /** Encuentra un nombre identificativo razonable para mostrar un registro
     *  relacionado (en una tabla o en un combo): numero de documento, nombre...
     *  Si no hay nada mejor, se usa el id. */
    public static String de(JsonNode item) {
        if (item == null || item.isNull()) {
            return "";
        }
        for (String campo : CAMPOS_ETIQUETA) {
            JsonNode valor = item.get(campo);
            if (valor != null && !valor.isNull() && !valor.asText().isBlank()) {
                return valor.asText();
            }
        }
        return "#" + item.path("id").asText();
    }
}
