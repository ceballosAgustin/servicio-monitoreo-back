package com.techforb.aceballos.servicio_monitoreo.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techforb.aceballos.servicio_monitoreo.dtos.UsuarioDto;
import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;
import com.techforb.aceballos.servicio_monitoreo.services.IUsuarioService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "*")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }

        try {
            Usuario usuarioCrear = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCrear);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<?> obtenerUsuario(Authentication authentication) {
        try {
            if (authentication == null || authentication.getName() == null) {
                throw new IllegalArgumentException("No hay un usuario autenticado");
            }

            UsuarioDto usuarioDto = usuarioService.findByEmail(authentication.getName());

            return ResponseEntity.ok(usuarioDto);

        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> borrarUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioOptional = usuarioService.borrarUsuario(id);
            return ResponseEntity.ok(usuarioOptional.orElseThrow());
            
        } catch (IllegalArgumentException  e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
