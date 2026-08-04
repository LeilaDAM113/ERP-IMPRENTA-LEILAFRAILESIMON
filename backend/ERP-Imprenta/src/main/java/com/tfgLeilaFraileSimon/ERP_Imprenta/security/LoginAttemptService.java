package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

/*
 * PROTECCION CONTRA FUERZA BRUTA en el login.
 * Lleva la cuenta de cuantos intentos fallidos SEGUIDOS lleva cada email.
 * Si un email llega a MAX_INTENTOS fallos, se queda "bloqueado" durante
 * TIEMPO_BLOQUEO, aunque despues escriba la contrasena correcta.
 *
 * OJO (para que quede claro en la memoria del TFG): esto se guarda en memoria
 * (un Map normal, no en la base de datos). Es suficiente para el TFG, con una
 * unica instancia del backend corriendo, pero si el servidor se reinicia el
 * contador se pierde, y si hubiera varias instancias a la vez cada una
 * llevaria su propia cuenta por separado. Para produccion de verdad se
 * guardaria esto en Redis o en la base de datos.
 */
@Service
public class LoginAttemptService {
    private static final int MAX_INTENTOS = 5;
    private static final Duration TIEMPO_BLOQUEO = Duration.ofMinutes(15);

    private final ConcurrentHashMap<String, Intento> intentosPorEmail = new ConcurrentHashMap<>();

    // Se llama cuando el login de ese email falla (contrasena incorrecta)
    public void registrarFallo(String email) {
        Intento intento = intentosPorEmail.computeIfAbsent(email, clave -> new Intento());
        intento.fallos.incrementAndGet();
        intento.ultimoFallo = Instant.now();
    }

    // Se llama cuando el login de ese email tiene EXITO -> se olvidan los fallos previos
    public void registrarExito(String email) {
        intentosPorEmail.remove(email);
    }

    // Dice si ese email esta bloqueado AHORA MISMO por demasiados fallos recientes
    public boolean estaBloqueado(String email) {
        Intento intento = intentosPorEmail.get(email);
        if (intento == null || intento.fallos.get() < MAX_INTENTOS) {
            return false;
        }

        boolean yaPasoElTiempoDeBloqueo = Instant.now().isAfter(intento.ultimoFallo.plus(TIEMPO_BLOQUEO));
        if (yaPasoElTiempoDeBloqueo) {
            // Ha pasado el tiempo de bloqueo: le damos otra oportunidad desde cero
            intentosPorEmail.remove(email);
            return false;
        }

        return true;
    }

    // Pequena clase interna que solo guarda el estado de intentos de un email
    private static class Intento {
        private final AtomicInteger fallos = new AtomicInteger(0);
        private volatile Instant ultimoFallo = Instant.now();
    }
}
