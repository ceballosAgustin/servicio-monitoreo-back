package com.techforb.aceballos.servicio_monitoreo.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un fórmato válido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La clave no puede estar vacía")
    private String clave;
}
