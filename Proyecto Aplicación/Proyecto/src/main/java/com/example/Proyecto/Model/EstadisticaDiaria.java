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
@Table(name = "Estadisticas_Diarias")
public class EstadisticaDiaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadistica")
    private Long idEstadisticaDia;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "Calorias", nullable = false)
    private Float calorias;

    @Column(name = "Proteinas")
    private Float proteinas;

    @Column(name = "Carbohidratos")
    private Float carbohidratos;

    @Column(name = "Grasas")
    private Float grasas;

    @Column(name = "Azucares")
    private Float azucares;

    @Column(name = "Fibra")
    private Float fibra;

    @Column(name = "Sodio")
    private Float sodio;

    @Column(name = "Grasas_Saturadas")
    private Float grasasSaturadas;

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

}

