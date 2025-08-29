package com.example.Proyecto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutrientesRecomendadosDTO {
    private Float calorias;
    private Float proteinas;      // en gramos
    private Float carbohidratos;  // en gramos
    private Float grasas;         // en gramos
    private Float azucares;
    private Float fibra;
    private Float sodio;
    private Float grasasSaturadas;
}

