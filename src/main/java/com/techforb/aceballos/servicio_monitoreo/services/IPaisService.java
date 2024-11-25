package com.techforb.aceballos.servicio_monitoreo.services;

import java.util.List;
import java.util.Map;

public interface IPaisService {
    List<Map<String, String>> obtenerPaises();
    
    String obtenerBanderaPais(String nombrePais);
}
