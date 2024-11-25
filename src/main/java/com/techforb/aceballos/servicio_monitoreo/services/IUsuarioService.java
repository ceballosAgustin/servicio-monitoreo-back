package com.techforb.aceballos.servicio_monitoreo.services;

import java.util.Optional;

import com.techforb.aceballos.servicio_monitoreo.dtos.UsuarioDto;
import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;

public interface IUsuarioService {

    public Usuario crearUsuario(Usuario usuario);

    public UsuarioDto findByEmail(String email);

    public Optional<Usuario> borrarUsuario(long idUsuario);

}
