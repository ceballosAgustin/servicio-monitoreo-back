package com.techforb.aceballos.servicio_monitoreo.entities;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un fórmato válido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La clave no puede estar vacía")
    @Length(min = 9, max = 256, message = "La clave debe tener una longitud superior a 8 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clave;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Length(min = 2, max = 128)
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío")
    @Length(min = 2, max = 128)
    private String apellido;

}
