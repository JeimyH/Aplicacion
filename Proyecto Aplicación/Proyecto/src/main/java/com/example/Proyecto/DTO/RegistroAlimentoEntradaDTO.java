package com.example.Proyecto.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAlimentoEntradaDTO {
    private Long idUsuario;
    private Long idAlimento;
    private Float tamanoPorcion;   // Cantidad convertida (en gramos)
    private String unidadMedida;   // Unidad convertida (gramos)
    private Float tamanoOriginal;  // Cantidad original (ej. 1 vaso)
    private String unidadOriginal; // Unidad original (ej. vaso)
    private String momentoDelDia;
}

