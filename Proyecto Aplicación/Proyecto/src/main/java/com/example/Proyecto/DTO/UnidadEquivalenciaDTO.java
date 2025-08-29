package com.example.Proyecto.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnidadEquivalenciaDTO {
    private Long idAlimento;
    private String unidadOrigen;
    private String unidadDestino;
    private Float factorConversion;
}

