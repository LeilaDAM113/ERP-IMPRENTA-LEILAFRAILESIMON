package com.tfgLeilaFraileSimon.ERP_Imprenta.config;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

/*
 * Configuracion de los tokens JWT.
 *  - JwtEncoder: FIRMA los tokens nuevos (se usa en el login).
 *  - JwtDecoder: COMPRUEBA los tokens que llegan en cada peticion
 *    (firma correcta y que no haya caducado). Spring lo usa solo.
 * Los dos usan la misma clave secreta (app.jwt.secreto, en local.properties)
 * con el algoritmo HS256.
 */
@Configuration
public class JwtConfig {

    @Value("${app.jwt.secreto}")
    private String secreto;

    private SecretKey clave() {
        return new SecretKeySpec(secreto.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(clave()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(clave()).macAlgorithm(MacAlgorithm.HS256).build();
    }
}