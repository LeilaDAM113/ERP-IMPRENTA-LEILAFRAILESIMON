package com.tfgLeilaFraileSimon.ERP_Imprenta.auth;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
// crea el token cuando alguien hace login
// lleva el email, el rol, cuando se crea y cuando caduca
// idea para el futuro: verificacion en dos pasos (2FA) al hacer login
@Service
public class TokenService {
    private final JwtEncoder encoder;

    @Value("${app.jwt.minutos-validez:480}")
    private long minutosValidez;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generarToken(Trabajador trabajador) {
        Instant ahora = Instant.now();
        JwtClaimsSet datos = JwtClaimsSet.builder()
                .subject(trabajador.getEmail())
                .claim("rol", trabajador.getRol().name())
                .issuedAt(ahora)
                .expiresAt(ahora.plus(minutosValidez, ChronoUnit.MINUTES))
                .build();
        JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(cabecera, datos)).getTokenValue();
    }

    public long segundosValidez() {
        return minutosValidez * 60;
    }
}
