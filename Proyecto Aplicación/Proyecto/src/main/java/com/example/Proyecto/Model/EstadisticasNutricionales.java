package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Estadisticas_Nutricionales")
public class EstadisticasNutricionales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_estadistica;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "Total_Calorias", nullable = false)
    private float totalCalorias;

    @Column(name = "Total_Proteinas")
    private float totalProteinas;

    @Column(name = "Total_Carbohidratos")
    private float totalCarbohidratos;

    @Column(name = "Total_Grasas")
    private float totalGrasas;

    @Column(name = "Total_Azucares")
    private float totalAzucares;

    @Column(name = "Total_Fibra")
    private float totalFibra;

    @Column(name = "Total_Sodio")
    private float totalSodio;

    @Column(name = "Total_Grasas_Saturadas")
    private float totalGrasasSaturadas;

    @Column(name = "Total_Agua")
    private float totalAgua;

    @Column(name = "Total_Comidas")
    private int totalComidas;

    @Column(name = "IMC", nullable = false)
    private float imc;

    //@Column(name = "Objetivos_Cumplidos")
    //private String objetivosCumplidos;

    @Column(name = "Calorias_Desayuno")
    private float caloriasDesayuno;

    @Column(name = "Calorias_Almuerzo")
    private float caloriasAlmuerzo;

    @Column(name = "Calorias_Cena")
    private float caloriasCena;

    @Column(name = "Calorias_Snack")
    private float caloriasSnack;

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

}
