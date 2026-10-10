package com.tfgLeilaFraileSimon.ERP_Imprenta.config;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/*
 * Documentacion de la API en Swagger (http://localhost:8080/swagger-ui.html).
 * Anade el boton "Authorize" para pegar el token y probar los endpoints protegidos.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "API de Sprinta", version = "1.0"),
        security = @SecurityRequirement(name = "token"))
@SecurityScheme(name = "token", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {
}