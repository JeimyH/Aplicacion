package com.example.Proyecto.DTO;

import lombok.Data;

@Data
public class RegistroAguaRespuestaDTO {
    private Long idRegistroAgua;
    private Long idUsuario;
    private String fecha;
    private int cantidadml;

}