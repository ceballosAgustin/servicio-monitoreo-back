package com.techforb.aceballos.servicio_monitoreo.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techforb.aceballos.servicio_monitoreo.dtos.UsuarioDto;
import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;
import com.techforb.aceballos.servicio_monitoreo.repositories.IUsuarioRepository;
import com.techforb.aceballos.servicio_monitoreo.services.IUsuarioService;

@Service
public class UsuarioService implements IUsuarioService{

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        if(usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso, intente con otro");
        }

        usuario.setClave(passwordEncoder.encode(usuario.getClave()));

        return usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioDto findByEmail(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario con email " + email + " no se pudo encontrar");
        }

        Usuario usuario = usuarioOptional.get();

        return new UsuarioDto(usuario.getNombre(), usuario.getApellido());
    }

    @Override
    public Optional<Usuario> borrarUsuario(long idUsuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

        if(!usuarioOptional.isPresent()) {
            throw new IllegalArgumentException("Usuario con id " + idUsuario + " no existe, no se puede eliminar");
        }

        usuarioRepository.deleteById(idUsuario);
        return usuarioOptional;
    }
}
