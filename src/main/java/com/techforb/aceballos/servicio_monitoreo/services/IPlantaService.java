package com.techforb.aceballos.servicio_monitoreo.services;

import java.util.List;
import java.util.Optional;

import com.techforb.aceballos.servicio_monitoreo.entities.Planta;

public interface IPlantaService {

    public Planta traerPlanta(Long idPlanta);

    public List<Planta> traerPlantas();

    public Planta crearPlanta(Planta planta);

    public Optional<Planta> modificarPlanta(long idPlanta, Planta planta);

    public Optional<Planta> borrarPlanta(long idPlanta);

    public Long obtenerLecturas();

    public Long obtenerAlertasMedias();
    
    public Long obtenerAlertasRojas();

    public Long obtenerSensoresDeshabilitados();

    public boolean existePorNombre(String nombre);
}
