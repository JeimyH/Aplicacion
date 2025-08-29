package com.example.Proyecto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaPorDiaDTO {
    private int dia;
    private NutrientesTotalesDTO nutrientes;
}
