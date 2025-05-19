package com.example.Proyecto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResumenDiarioDTO {

    private Float totalCalorias, totalProteinas, totalCarbohidratos, totalGrasas, totalAzucares, totalFibra, totalSodio, totalGrasasSaturadas;
}
