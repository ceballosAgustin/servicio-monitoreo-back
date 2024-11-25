package com.techforb.aceballos.servicio_monitoreo.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.aceballos.servicio_monitoreo.services.IPaisService;

@Service
public class PaisService implements IPaisService {

    private final String apiUrl = "https://restcountries.com/v3.1/all";
    private final RestTemplate restTemplate;

    public PaisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public String obtenerBanderaPais(String nombrePais) {
        String url = "https://restcountries.com/v3.1/name/" + nombrePais;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> paises = objectMapper.readValue(response.getBody(), List.class);

                if(paises != null && !paises.isEmpty()) {
                    Map<String, Object> pais = paises.get(0);
                    Map<String, Object> banderas = (Map<String, Object>) pais.get("flags");

                    return (String) banderas.get("svg");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener la bandera del país " + nombrePais, e);
        }

        return null;
    }
}

