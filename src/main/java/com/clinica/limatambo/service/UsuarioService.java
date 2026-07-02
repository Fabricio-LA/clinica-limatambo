package com.clinica.limatambo.service;

import com.clinica.limatambo.model.Rol;
import com.clinica.limatambo.model.Usuario;
import com.clinica.limatambo.repository.RolRepository;
import com.clinica.limatambo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (usuario.getEstado() != null && !usuario.getEstado()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        Rol rol = rolRepository.findById(usuario.getIdRol())
                .orElseThrow(() -> new UsernameNotFoundException("Rol no encontrado para el usuario: " + username));

        String nombreRol = rol.getNombreRol().toUpperCase();
        if (nombreRol.equals("ADMINISTRADOR")) nombreRol = "ADMIN";
        if (nombreRol.equals("RECEPCIONISTA")) nombreRol = "RECEPCION";

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + nombreRol))
        );
    }
}
