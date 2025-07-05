package com.example.Proyecto.DTO;

import java.time.LocalDate;

public class ActividadDiaDTO {
    private LocalDate fecha;
    private String tipo; // "AGUA", "COMIDA", "AMBOS"

    public ActividadDiaDTO(LocalDate fecha, String tipo) {
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }
}

