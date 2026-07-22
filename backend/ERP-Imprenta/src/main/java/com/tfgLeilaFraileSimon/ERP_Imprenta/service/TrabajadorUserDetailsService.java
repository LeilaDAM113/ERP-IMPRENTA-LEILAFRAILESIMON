package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;

/*
 * EL "PORTERO" QUE BUSCA AL USUARIO CUANDO ALGUIEN INTENTA ENTRAR.
 * Spring Security llama a esta clase automaticamente en cada login, pasandole
 * el email que ha escrito la persona. Aqui:
 *   1. buscamos ese email en la base de datos (con el repositorio)
 *   2. si existe -> lo devolvemos tal cual (Trabajador ya implementa UserDetails)
 *   3. si no existe -> lanzamos error y Spring rechaza el acceso
 * Ojo: aqui NO comparamos la contrasena; de eso se encarga Spring por su cuenta.
 */
@Service
public class TrabajadorUserDetailsService implements UserDetailsService {
    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorUserDetailsService(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return trabajadorRepository.findByEmail(email)
                .map(UserDetails.class::cast)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un trabajador con email " + email));
    }
}
