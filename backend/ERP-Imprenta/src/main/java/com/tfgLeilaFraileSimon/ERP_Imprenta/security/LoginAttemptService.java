package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

// Proteccion para que nadie pueda probar contraseñas sin parar.
// Cuenta los fallos seguidos de cada email y, si llega a 5,
// lo bloquea 15 minutos (aunque luego ponga bien la contraseña).
// Los fallos se guardan en memoria, no en la base de datos:
// si reinicio el servidor, el contador vuelve a 0.
@Service
public class LoginAttemptService {
    private static final int MAX_INTENTOS = 5;
    private static final Duration TIEMPO_BLOQUEO = Duration.ofMinutes(15);

    private final ConcurrentHashMap<String, Intento> intentosPorEmail = new ConcurrentHashMap<>();

    // cuando alguien falla la contraseña, sumo un fallo a ese email
    public void registrarFallo(String email) {
        Intento intento = intentosPorEmail.computeIfAbsent(email, clave -> new Intento());
        intento.fallos.incrementAndGet();
        intento.ultimoFallo = Instant.now();
    }

    // cuando acierta, borro sus fallos
    public void registrarExito(String email) {
        intentosPorEmail.remove(email);
    }

    // dice si ese email esta bloqueado ahora mismo
    public boolean estaBloqueado(String email) {
        Intento intento = intentosPorEmail.get(email);
        if (intento == null || intento.fallos.get() < MAX_INTENTOS) {
            return false;
        }

        boolean yaPasoElTiempoDeBloqueo = Instant.now().isAfter(intento.ultimoFallo.plus(TIEMPO_BLOQUEO));
        if (yaPasoElTiempoDeBloqueo) {
            // ya han pasado los 15 minutos: empieza de cero
            intentosPorEmail.remove(email);
            return false;
        }

        return true;
    }

    // guarda los fallos de un email y cuando fue el ultimo
    private static class Intento {
        private final AtomicInteger fallos = new AtomicInteger(0);
        private volatile Instant ultimoFallo = Instant.now();
    }
}
