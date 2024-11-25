package com.techforb.aceballos.servicio_monitoreo.controllers;

import static com.techforb.aceballos.servicio_monitoreo.configuration.TokenJwtConfig.SECRET_KEY;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techforb.aceballos.servicio_monitoreo.dtos.LoginDto;
import com.techforb.aceballos.servicio_monitoreo.dtos.UsuarioDto;
import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;
import com.techforb.aceballos.servicio_monitoreo.services.IUsuarioService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult result) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getClave())
            );

            User userDetails = (User) authentication.getPrincipal();
            String email = userDetails.getUsername();
            UsuarioDto usuarioDb =  usuarioService.findByEmail(email);
            String nombre = "%s %s".formatted(usuarioDb.getNombre(), usuarioDb.getApellido());

            Claims claims = Jwts.claims()
                            .add("authorities", userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                            .add("email", email)
                            .add("name", nombre)
                            .build();

            String token = Jwts.builder()
                                .subject(email)
                                .claims(claims)
                                .expiration(new Date(System.currentTimeMillis() + 3600000))
                                .issuedAt(new Date())
                                .signWith(SECRET_KEY)
                                .compact();
                                
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("email", email);
            body.put("nombre", nombre);
            body.put("mensaje", String.format("Has iniciado sesión con exito!"));
        
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            Map<String, String> body = new HashMap<>();

            body.put("mensaje", "Error en la autenticación, email o clave incorrectos!");
            body.put("error", e.getMessage());
            return ResponseEntity.status(401).body(body);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }
        
        try {
            Usuario usuarioNuevo = usuarioService.crearUsuario(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "¡Perfecto!, usuario registrado con exito.",
                "nombre", usuarioNuevo.getNombre()
            ));
        } catch (IllegalArgumentException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "mensaje", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "mensaje", "¡Ups!, error inesperado al registrar el usuario.",
                "error", e.getMessage()
            ));
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
