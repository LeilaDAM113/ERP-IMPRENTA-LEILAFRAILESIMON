package com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo;

import java.util.List;

/** Equivalente a cada grupo de GRUPOS_NAV en frontend/js/app.js. */
public record GrupoNav(String titulo, List<ItemNav> items) {
}
