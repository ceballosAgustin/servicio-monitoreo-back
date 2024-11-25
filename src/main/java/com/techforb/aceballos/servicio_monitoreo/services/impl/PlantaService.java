package com.techforb.aceballos.servicio_monitoreo.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techforb.aceballos.servicio_monitoreo.entities.Planta;
import com.techforb.aceballos.servicio_monitoreo.repositories.IPlantaRepository;
import com.techforb.aceballos.servicio_monitoreo.services.IPaisService;
import com.techforb.aceballos.servicio_monitoreo.services.IPlantaService;


@Service
public class PlantaService implements IPlantaService{

    @Autowired
    private IPlantaRepository plantaRepository;

    @Autowired
    private IPaisService paisService;

    @Override
    @Transactional(readOnly = true)
    public Planta traerPlanta(Long idPlanta) {
        return plantaRepository.findById(idPlanta)
                .orElseThrow(() -> new IllegalArgumentException("Planta con id " + idPlanta + " no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Planta> traerPlantas() {
        return plantaRepository.findAll();
    }

    @Override
    @Transactional
    public Planta crearPlanta(Planta planta) {
        planta.setNombre(planta.getNombre().trim());

        if(existePorNombre(planta.getNombre())) {
            throw new IllegalArgumentException("El nombre de la planta ya está en uso, escoja otro");
        }

        String bandera = paisService.obtenerBanderaPais(planta.getPais());
        planta.setBandera(bandera);

        return plantaRepository.save(planta);
    }

    @Override
    @Transactional
    public Optional<Planta> modificarPlanta(long idPlanta, Planta planta) {
        Optional<Planta> plantaOptional = plantaRepository.findById(idPlanta);

        if(!plantaOptional.isPresent()) {
            throw new IllegalArgumentException("Planta con id " + idPlanta + " no existe, no se puede modificar");
        }
        
        Planta plantaModificar = plantaOptional.orElseThrow();
        plantaModificar.setLectura(planta.getLectura());
        plantaModificar.setAlertaMedia(planta.getAlertaMedia());
        plantaModificar.setAlertaRoja(planta.getAlertaRoja());
        plantaModificar.setSensorDeshabilitado(planta.getSensorDeshabilitado());

        return Optional.of(plantaRepository.save(plantaModificar));
    }

    @Override
    @Transactional
    public Optional<Planta> borrarPlanta(long idPlanta) {
        Optional<Planta> plantaOptional = plantaRepository.findById(idPlanta);

        if(!plantaOptional.isPresent()) {
            throw new IllegalArgumentException("Planta con id " + idPlanta + " no existe, no se puede eliminar"); 
        }
        
        plantaRepository.deleteById(idPlanta);
        return plantaOptional;
    }

    @Override
    public Long obtenerLecturas() {
        return plantaRepository.cantLecturas();
    }

    @Override
    public Long obtenerAlertasMedias() {
        return plantaRepository.cantAlertasMedias();
    }

    @Override
    public Long obtenerAlertasRojas() {
        return plantaRepository.cantAlertasRojas();
    }

    @Override
    public Long obtenerSensoresDeshabilitados() {
        return plantaRepository.cantSensoresDeshabilitados();
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return plantaRepository.existsByNombre(nombre);
    }
}
