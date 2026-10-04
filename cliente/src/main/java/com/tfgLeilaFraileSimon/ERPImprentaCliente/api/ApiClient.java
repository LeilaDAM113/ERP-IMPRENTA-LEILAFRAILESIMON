package com.tfgLeilaFraileSimon.ERPImprentaCliente.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Equivalente a frontend/js/api.js: cliente HTTP muy pequeno para hablar con
 * /api/... El backend usa autenticacion Basic (email + contrasena en cada
 * peticion, ver SecurityConfig.java del backend), asi que aqui tampoco hay
 * "sesion" del lado del servidor: guardamos las credenciales codificadas en
 * Base64 en memoria (se pierden al cerrar la app, igual que el sessionStorage
 * del navegador se pierde al cerrar la pestana) y se mandan en la cabecera
 * Authorization en cada llamada.
 */
public class ApiClient {

    private static final String URL_BASE = "http://localhost:8080/api";

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private String credenciales; // Base64("email:password"), o null si no hay sesion iniciada

    public void guardarCredenciales(String email, String password) {
        credenciales = Base64.getEncoder().encodeToString((email + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    public void borrarCredenciales() {
        credenciales = null;
    }

    public boolean haySesion() {
        return credenciales != null;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public JsonNode llamar(String ruta) throws ErrorApi {
        return llamar(ruta, "GET", null);
    }

    /** cuerpo, si no es null, se manda ya convertido a JSON (ver ENTIDADES/FormularioGenericoDialog). */
    public JsonNode llamar(String ruta, String metodo, JsonNode cuerpo) throws ErrorApi {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(URL_BASE + ruta));
        if (credenciales != null) {
            builder.header("Authorization", "Basic " + credenciales);
        }

        BodyPublisher publicador = BodyPublishers.noBody();
        if (cuerpo != null) {
            try {
                publicador = BodyPublishers.ofString(mapper.writeValueAsString(cuerpo));
            } catch (IOException e) {
                throw new ErrorApi(-1, "No se ha podido preparar la peticion: " + e.getMessage());
            }
            builder.header("Content-Type", "application/json");
        }
        builder.method(metodo, publicador);

        HttpResponse<String> respuesta;
        try {
            respuesta = http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ErrorApi(-1, "No se ha podido conectar con el servidor. ¿Esta arrancado el backend?");
        }

        if (respuesta.statusCode() == 401) {
            borrarCredenciales();
        }
        if (respuesta.statusCode() < 200 || respuesta.statusCode() >= 300) {
            throw new ErrorApi(respuesta.statusCode(), extraerMensajeError(respuesta.body(), respuesta.statusCode()));
        }

        String cuerpoTexto = respuesta.body();
        if (cuerpoTexto == null || cuerpoTexto.isBlank()) {
            return null;
        }
        try {
            return mapper.readTree(cuerpoTexto);
        } catch (IOException e) {
            throw new ErrorApi(respuesta.statusCode(), "Respuesta invalida del servidor.");
        }
    }

    // Igual que extraerMensajeError() de api.js: los errores de validacion (@Valid)
    // llegan como un array "errors" con un mensaje cada uno; el resto llega en "message".
    private String extraerMensajeError(String cuerpo, int status) {
        try {
            JsonNode nodo = mapper.readTree(cuerpo);
            JsonNode errores = nodo.path("errors");
            if (errores.isArray() && !errores.isEmpty()) {
                StringBuilder mensaje = new StringBuilder();
                for (JsonNode error : errores) {
                    if (!mensaje.isEmpty()) {
                        mensaje.append(". ");
                    }
                    mensaje.append(error.path("defaultMessage").asText());
                }
                return mensaje.toString();
            }
            if (nodo.hasNonNull("message")) {
                return nodo.get("message").asText();
            }
        } catch (Exception ignorado) {
            // Cuerpo no era JSON valido: nos quedamos con el mensaje generico de abajo.
        }
        return "Error " + status;
    }
}
