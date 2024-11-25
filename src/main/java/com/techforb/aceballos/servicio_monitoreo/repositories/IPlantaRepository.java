package com.techforb.aceballos.servicio_monitoreo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techforb.aceballos.servicio_monitoreo.entities.Planta;

@Repository
public interface IPlantaRepository extends JpaRepository<Planta, Long>{

    public Optional<Planta> findByNombre(String nombre);
    
    public boolean existsByNombre(String nombre);

    @Query("SELECT COALESCE(SUM(p.lectura), 0) FROM Planta p")
    Long cantLecturas();

    @Query("SELECT COALESCE(SUM(p.alertaMedia), 0) FROM Planta p")
    Long cantAlertasMedias();

    @Query("SELECT COALESCE(SUM(p.alertaRoja), 0) FROM Planta p")
    Long cantAlertasRojas();

    @Query("SELECT COALESCE(SUM(p.sensorDeshabilitado), 0) FROM Planta p")
    Long cantSensoresDeshabilitados();
}
