package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

// Lo que devuelve el login: el token, cuanto dura (en segundos)
// y los datos del trabajador, para que la app sepa su nombre y su rol.
public record LoginResponse(String token, long expiraEnSegundos, TrabajadorResponse trabajador) {
}