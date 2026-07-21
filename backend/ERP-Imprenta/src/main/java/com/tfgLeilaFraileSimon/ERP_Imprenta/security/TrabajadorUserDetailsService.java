package com.tfgLeilaFraileSimon.ERP_Imprenta.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;

@Service
public class TrabajadorUserDetailsService implements UserDetailsService {
    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorUserDetailsService(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return trabajadorRepository.findByEmail(email)
                .map(TrabajadorUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un trabajador con email " + email));
    }
}
