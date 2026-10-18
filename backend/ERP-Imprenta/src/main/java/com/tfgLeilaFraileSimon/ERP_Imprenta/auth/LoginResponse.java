package com.tfgLeilaFraileSimon.ERP_Imprenta.auth;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorResponse;

// lo que devuelve el login: el token, lo que dura (en segundos) y los datos del trabajador
public record LoginResponse(String token, long expiraEnSegundos, TrabajadorResponse trabajador) {
}