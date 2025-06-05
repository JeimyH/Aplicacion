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
@Table(name = "Comida_Rutina_IA")
public class ComidaRutinaIA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comida")
    private Long idComida;

    @Column(name = "Tipo_Comida", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoComida tipoComida;

    @Column(name = "Alimentos_Sugeridos")
    private String alimentosSugeridos;

    @Column(name = "ValoresNutricionales")
    private String valoresNutricionales;

    @Column(name = "Tamano_Porciones")
    private String tamanoPorciones;

    @Column(name = "Dia_Numero", nullable = false)
    private int diaNumero;

    @Column(name = "Dia_Semana", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    public enum TipoComida{
        Desayuno,
        Almuerzo,
        Cena,
        Snack
    }

    public enum DiaSemana{
        Lunes,
        Martes,
        Miercoles,
        Jueves,
        Vierner,
        Sabado,
        Domingo
    }

    // Relaciones

    @ManyToOne
    @JoinColumn(name="id_rutina", nullable = false)
    @JsonIgnore
    private RutinaAlimenticiaIA rutina;
}
