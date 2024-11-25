package com.techforb.aceballos.servicio_monitoreo.configuration.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.aceballos.servicio_monitoreo.entities.Usuario;
import com.techforb.aceballos.servicio_monitoreo.repositories.IUsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.techforb.aceballos.servicio_monitoreo.configuration.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private AuthenticationManager authenticationManager;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        Usuario usuario = null;
        String email = null;
        String clave = null;

        try {
            usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            email = usuario.getEmail();
            clave = usuario.getClave();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, clave);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
            org.springframework.security.core.userdetails.User usuario = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
            String email = usuario.getUsername();
            Usuario usuarioDb =  usuarioRepository.findByEmail(email).get();
            String nombre = "%s %s".formatted(usuarioDb.getNombre(), usuarioDb.getApellido());

            Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

            Claims claims = Jwts.claims()
                            .add("authorities", new ObjectMapper().writeValueAsString(roles))
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

            response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("email", email);
            body.put("name", nombre);
            body.put("message", String.format("Has iniciado sesión con exito!"));

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
        Map<String, String> body = new HashMap<>();

        body.put("message", "Error en la autenticación, email o clave incorrectos!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
    }
}
