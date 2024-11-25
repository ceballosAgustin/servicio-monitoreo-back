package com.techforb.aceballos.servicio_monitoreo.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techforb.aceballos.servicio_monitoreo.entities.Planta;
import com.techforb.aceballos.servicio_monitoreo.services.IPlantaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "*")
public class PlantaController {

    @Autowired
    private IPlantaService plantaService;

    @GetMapping("/plantas/{id}")
    public ResponseEntity<?> traerPlanta(@PathVariable Long id) {
        try {
            Planta planta = plantaService.traerPlanta(id);
            return ResponseEntity.ok(planta);

        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/plantas")
    public ResponseEntity<?> traerPlantas() {
        List<Planta> plantas = plantaService.traerPlantas();
        return ResponseEntity.ok(plantas);
    }

    @PostMapping("/plantas")
    public ResponseEntity<?> crearPlanta(@Valid @RequestBody Planta planta, BindingResult result) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }

        try {
            Planta plantaCrear = plantaService.crearPlanta(planta);
            return ResponseEntity.status(HttpStatus.CREATED).body(plantaCrear);
        
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping("/plantas/{id}")
    public ResponseEntity<?> modificarPlanta(@Valid @RequestBody Planta planta, BindingResult result, @PathVariable Long id) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }

        try {
            Optional<Planta> plantaOptional = plantaService.modificarPlanta(id, planta);
            return ResponseEntity.ok(plantaOptional.orElseThrow());
        
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/plantas/{id}")
    public ResponseEntity<?> borrarPlanta(@PathVariable Long id) {
        try {
            Optional<Planta> plantaOptional = plantaService.borrarPlanta(id);
            return ResponseEntity.ok(plantaOptional.orElseThrow());
        
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/plantas/lecturas")
    public ResponseEntity<Long> obtenerLecturas() {
        Long totalLecturas = plantaService.obtenerLecturas();
        return ResponseEntity.ok(totalLecturas);
    }

    @GetMapping("/plantas/alertas-medias")
    public ResponseEntity<Long> obtenerAlertasMedias() {
        Long totalAlertasMedias = plantaService.obtenerAlertasMedias();
        return ResponseEntity.ok(totalAlertasMedias);
    }

    @GetMapping("/plantas/alertas-rojas")
    public ResponseEntity<Long> obtenerAlertasRojas() {
        Long totalAlertasRojas = plantaService.obtenerAlertasRojas();
        return ResponseEntity.ok(totalAlertasRojas);
    }

    @GetMapping("/plantas/sensores-deshabilitados")
    public ResponseEntity<Long> obtenerSensoresDeshabilitados() {
        Long totalSensoresDeshabilitados = plantaService.obtenerSensoresDeshabilitados();
        return ResponseEntity.ok(totalSensoresDeshabilitados);
    }

    @GetMapping("/plantas/verificar-nombre")
    public ResponseEntity<?> verificarNombre(@RequestParam String nombre) {
        boolean existe = plantaService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
