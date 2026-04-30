package com.clinica.limatambo.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin01".equals(username)) {
            return new User("admin01", "admin123", 
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        } else if ("medico01".equals(username)) {
            return new User("medico01", "medico123", 
                List.of(new SimpleGrantedAuthority("ROLE_MEDICO")));
        } else if ("12345678".equals(username)) {
            return new User("12345678", "123456", 
                List.of(new SimpleGrantedAuthority("ROLE_PACIENTE")));
        }
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}