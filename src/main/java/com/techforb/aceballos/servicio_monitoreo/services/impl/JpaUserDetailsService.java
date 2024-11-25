package com.techforb.aceballos.servicio_monitoreo.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;
import com.techforb.aceballos.servicio_monitoreo.repositories.IUsuarioRepository;
import com.techforb.aceballos.servicio_monitoreo.services.IJpaUserDetailsService;

@Service
public class JpaUserDetailsService implements IJpaUserDetailsService{

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        
        if(usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario inexistente en el sistema");
        }

        Usuario usuario = usuarioOptional.orElseThrow();

        return org.springframework.security.core.userdetails.User.builder()
                                                                    .username(usuario.getEmail())
                                                                    .password(usuario.getClave())
                                                                    .build();
    
    }

}
