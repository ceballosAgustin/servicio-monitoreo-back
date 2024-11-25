package com.techforb.aceballos.servicio_monitoreo.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techforb.aceballos.servicio_monitoreo.services.IPaisService;

@RestController
@RequestMapping("/api")
public class PaisController {

    @Autowired
    private IPaisService paisService;

    @GetMapping("/paises")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Map<String, String>>> obtenerPaises() {
        try {
            List<Map<String, String>> paises = paisService.obtenerPaises();
            return ResponseEntity.ok(paises);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Collections.singletonList(Map.of("error", "Error al obtener los paises")));
        }
    }
}


