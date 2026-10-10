package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

/*
 * Respuesta del login: el token, cuantos segundos dura y los datos del
 * trabajador (para que la app sepa su nombre y su rol sin otra peticion).
 */
public record LoginResponse(String token, long expiraEnSegundos, TrabajadorResponse trabajador) {
}