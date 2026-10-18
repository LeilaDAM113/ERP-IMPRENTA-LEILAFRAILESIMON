package com.tfgLeilaFraileSimon.ERP_Imprenta.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

// para que no puedan probar contraseñas sin parar:
// a los 5 fallos seguidos bloqueo ese email 15 minutos
// los fallos se guardan en memoria, si reinicio el servidor se borran
@Service
public class LoginAttemptService {
    private static final int MAX_INTENTOS = 5;
    private static final Duration TIEMPO_BLOQUEO = Duration.ofMinutes(15);

    private final ConcurrentHashMap<String, Intento> intentosPorEmail = new ConcurrentHashMap<>();

    // sumo un fallo
    public void registrarFallo(String email) {
        Intento intento = intentosPorEmail.computeIfAbsent(email, clave -> new Intento());
        intento.fallos.incrementAndGet();
        intento.ultimoFallo = Instant.now();
    }

    // borro los fallos
    public void registrarExito(String email) {
        intentosPorEmail.remove(email);
    }

    // mira si el email esta bloqueado
    public boolean estaBloqueado(String email) {
        Intento intento = intentosPorEmail.get(email);
        if (intento == null || intento.fallos.get() < MAX_INTENTOS) {
            return false;
        }

        boolean yaPasoElTiempoDeBloqueo = Instant.now().isAfter(intento.ultimoFallo.plus(TIEMPO_BLOQUEO));
        if (yaPasoElTiempoDeBloqueo) {
            // ya han pasado los 15 minutos, empiezo de 0
            intentosPorEmail.remove(email);
            return false;
        }

        return true;
    }

    // los fallos de un email y cuando fue el ultimo
    private static class Intento {
        private final AtomicInteger fallos = new AtomicInteger(0);
        private volatile Instant ultimoFallo = Instant.now();
    }
}
