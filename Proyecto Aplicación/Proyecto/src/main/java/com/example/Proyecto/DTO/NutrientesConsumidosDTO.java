package com.example.Proyecto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NutrientesConsumidosDTO {
    private String nombreAlimento;
    private Float cantidadConsumida;
    private String unidadMedida;
    private Float calorias, proteinas, carbohidratos, grasas, azucares, fibra, sodio, grasasSaturadas;
}
