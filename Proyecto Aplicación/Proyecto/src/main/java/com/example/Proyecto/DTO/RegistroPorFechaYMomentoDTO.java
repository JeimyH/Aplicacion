package com.example.Proyecto.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroPorFechaYMomentoDTO {
    private Long idAlimento;
    private String nombreAlimento;
    private String categoria;
    private String urlImagen;
    private float tamanoPorcion;
    private String unidadMedida;
    private String momentoDelDia;
    private String consumidoEn;

    // Getters y setters
}

