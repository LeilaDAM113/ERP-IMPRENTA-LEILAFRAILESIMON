package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;

/*
 * EL "PORTERO" QUE BUSCA AL USUARIO CUANDO ALGUIEN INTENTA ENTRAR.
 * Implementa UserDetailsService: Spring Security lo llama en cada login con el
 * email escrito. Busca ese email en la BD; si existe lo devuelve tal cual
 * (Trabajador YA implementa UserDetails), y si no, lanza error y se rechaza.
 * La contrasena la compara Spring por su cuenta, aqui NO.
 */
@Service
public class TrabajadorService implements UserDetailsService {
    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorService(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return trabajadorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un trabajador con email " + email));
    }
}
