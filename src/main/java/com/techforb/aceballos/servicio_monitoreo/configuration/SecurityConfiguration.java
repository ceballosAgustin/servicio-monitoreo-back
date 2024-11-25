package com.techforb.aceballos.servicio_monitoreo.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.techforb.aceballos.servicio_monitoreo.configuration.filter.JwtAuthenticationFilter;
import com.techforb.aceballos.servicio_monitoreo.configuration.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authz) -> authz
        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/registro").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/usuario").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/{id}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/plantas", "/api/plantas/{id}").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/plantas").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/plantas/{id}").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/api/plantas/{id}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/plantas/lecturas").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/plantas/alertas-medias").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/plantas/alertas-rojas").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/plantas/sensores-deshabilitados").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/paises").permitAll()
        .anyRequest().authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return corsBean;
    }
}
