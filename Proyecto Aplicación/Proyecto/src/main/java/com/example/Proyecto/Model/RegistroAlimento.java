package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Registro_Alimento")
public class RegistroAlimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro_alimento")
    private Long idRegistroAlimento;

    @Column(name = "Tamano_Porcion", nullable = false)
    private Float tamanoPorcion;  // cantidad consumida del alimento

    @Column(name = "Unidad_Medida", length = 50, nullable = false)
    private String unidadMedida;  // taza, gramos, porcion

    @Column(name = "Favorito")
    private boolean favorito;

    @Column(name = "Momento_Del_Dia", length = 50) // Nuevo campo
    private String momentoDelDia;

    @Column(name = "Consumido_En", nullable = false)
    private Timestamp consumidoEn;  // fecha de consumo del alimento

    /*@PrePersist
    protected void onCreate() {
        this.consumidoEn = new Timestamp(System.currentTimeMillis());
    }*/

    //Relaciones entre tablas
    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="id_alimento", nullable = false)
    @JsonIgnore
    private Alimento alimento;

}
