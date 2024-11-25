package com.techforb.aceballos.servicio_monitoreo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{

    public Optional<Usuario> findByEmail(String email);

    public Usuario findByNombre(String nombre);

    public boolean existsByEmail(String email);
}
