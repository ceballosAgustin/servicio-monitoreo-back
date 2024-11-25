package com.techforb.aceballos.servicio_monitoreo.entities;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plantas")
public class Planta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_planta")
    private Long idPlanta;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El nombre de la planta no puede estar vacío")
    @Length(min = 4, max = 50)
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El país de la planta no puede estar vacío")
    private String pais;

    private String bandera;

    @Column(nullable = true, name = "lecturas")
    @ColumnDefault("0")
    private Long lectura;

    @Column(nullable = true, name = "alertas_medias")
    @ColumnDefault("0")
    private Long alertaMedia;

    @Column(nullable = true, name = "alertas_rojas")
    @ColumnDefault("0")
    private Long alertaRoja;

    @Column(nullable = true, name = "sensores_deshabilitados")
    @ColumnDefault("0")
    private Long sensorDeshabilitado;
}
