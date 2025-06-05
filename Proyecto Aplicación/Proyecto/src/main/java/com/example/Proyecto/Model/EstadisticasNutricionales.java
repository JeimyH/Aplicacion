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
    @Column(name = "id_estadistica")
    private Long idEstadistica;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "Total_Calorias", nullable = false)
    private Float totalCalorias;

    @Column(name = "Total_Proteinas")
    private Float totalProteinas;

    @Column(name = "Total_Carbohidratos")
    private Float totalCarbohidratos;

    @Column(name = "Total_Grasas")
    private Float totalGrasas;

    @Column(name = "Total_Azucares")
    private Float totalAzucares;

    @Column(name = "Total_Fibra")
    private Float totalFibra;

    @Column(name = "Total_Sodio")
    private Float totalSodio;

    @Column(name = "Total_Grasas_Saturadas")
    private Float totalGrasasSaturadas;

    @Column(name = "Total_Agua")
    private Float totalAgua;

    @Column(name = "Total_Comidas")
    private int totalComidas;

    @Column(name = "IMC", nullable = false)
    private Float imc;

    //@Column(name = "Objetivos_Cumplidos")
    //private String objetivosCumplidos;

    @Column(name = "Calorias_Desayuno")
    private Float caloriasDesayuno;

    @Column(name = "Calorias_Almuerzo")
    private Float caloriasAlmuerzo;

    @Column(name = "Calorias_Cena")
    private Float caloriasCena;

    @Column(name = "Calorias_Snack")
    private Float caloriasSnack;

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

}
