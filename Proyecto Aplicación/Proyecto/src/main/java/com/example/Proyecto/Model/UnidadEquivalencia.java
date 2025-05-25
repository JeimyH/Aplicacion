package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Unidad_Equivalencia")
public class UnidadEquivalencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad")
    private Long idUnidad;

    @Column(name = "Unidad_Origen", length = 50, nullable = false)
    private String unidadOrigen;

    @Column(name = "Unidad_Destino", length = 50, nullable = false)
    private String unidadDestino;

    @Column(name = "Factor_Conversion", nullable = false)
    private Float factorConversion;

    // Relaciones
    @ManyToOne
    @JoinColumn(name="id_alimento", nullable = false)
    @JsonIgnore
    private Alimento alimento;
}
