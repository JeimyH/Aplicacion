package com.example.Proyecto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UsuarioEntradaDTO {
    private String correo;
    private String contrasena;
    private String nombre;
    private LocalDate fechaNacimiento;
    private Float altura;
    private Float peso;
    private String sexo;
    private String restriccionesDieta;
    private String objetivosSalud;
    private Float pesoObjetivo;
}
