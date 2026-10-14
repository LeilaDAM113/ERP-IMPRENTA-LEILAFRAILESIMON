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

// Configuracion de los tokens.
// - jwtEncoder: crea y firma el token cuando alguien hace login
// - jwtDecoder: comprueba en cada peticion que el token es bueno y no ha caducado
// Los dos usan la clave secreta que tengo en local.properties.
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