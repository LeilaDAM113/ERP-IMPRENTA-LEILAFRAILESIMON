package com.tfgLeilaFraileSimon.sprinta.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfgLeilaFraileSimon.sprinta.modelo.Trabajador;

// todo lo que habla con la API pasa por aqui.
// manda las peticiones HTTP, pone el token y convierte el JSON en objetos Java
public class ApiCliente {
    // donde esta la API (mi portatil). Mas adelante ira en un archivo de configuracion
    private static final String URL_API = "http://localhost:8080";

    private static final HttpClient http = HttpClient.newHttpClient();
    // si la API manda un campo que no tengo en mis clases, lo ignoro (asi no se rompe)
    private static final ObjectMapper json = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // login: mando email y contraseña, y si estan bien guardo el token en la Sesion
    public static void login(String email, String password) throws ErrorApi {
        String cuerpo = escribir(Map.of("email", email, "password", password));
        HttpRequest peticion = HttpRequest.newBuilder(URI.create(URL_API + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                .build();
        JsonNode respuesta = leer(enviar(peticion));
        Trabajador trabajador = json.convertValue(respuesta.get("trabajador"), Trabajador.class);
        Sesion.iniciar(respuesta.get("token").asText(), trabajador);
    }

    // GET de una lista: por ejemplo listar("/api/cliente", Cliente.class)
    public static <T> List<T> listar(String ruta, Class<T> tipo) throws ErrorApi {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create(URL_API + ruta))
                .header("Authorization", "Bearer " + Sesion.getToken())
                .GET()
                .build();
        JsonNode respuesta = leer(enviar(peticion));
        return json.convertValue(respuesta,
                json.getTypeFactory().constructCollectionType(List.class, tipo));
    }

    // manda la peticion y, si la API contesta con error, lo convierto en ErrorApi con su mensaje
    private static HttpResponse<String> enviar(HttpRequest peticion) throws ErrorApi {
        HttpResponse<String> respuesta;
        try {
            respuesta = http.send(peticion, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ErrorApi(0, "No se puede conectar con el servidor. ¿Esta arrancada la API?");
        }
        if (respuesta.statusCode() >= 400) {
            throw new ErrorApi(respuesta.statusCode(), mensajeDeError(respuesta));
        }
        return respuesta;
    }

    // la API manda los errores como {"mensaje": "..."} (o {"message": "..."} en el 401 y el 403)
    private static String mensajeDeError(HttpResponse<String> respuesta) {
        try {
            JsonNode error = json.readTree(respuesta.body());
            if (error.hasNonNull("mensaje")) {
                return error.get("mensaje").asText();
            }
            if (error.hasNonNull("message")) {
                return error.get("message").asText();
            }
        } catch (IOException e) {
            // no era JSON: pongo un mensaje general
        }
        return "Error " + respuesta.statusCode();
    }

    private static JsonNode leer(HttpResponse<String> respuesta) throws ErrorApi {
        try {
            return json.readTree(respuesta.body());
        } catch (IOException e) {
            throw new ErrorApi(0, "La respuesta del servidor no se entiende");
        }
    }

    private static String escribir(Object objeto) {
        try {
            return json.writeValueAsString(objeto);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
