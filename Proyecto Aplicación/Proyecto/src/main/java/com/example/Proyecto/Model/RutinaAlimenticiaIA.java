package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Rutina_Alimentia_IA")
public class RutinaAlimenticiaIA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private Long idRutina;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Fecha_Inicio", nullable = false)
    private LocalDate fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Fecha_Fin")
    private LocalDate fechaFin;

    @Column(name = "Objetivo_Calorico_Dia", nullable = false)
    private Float objetivoCaloricoDia;

    @Column(name = "Dias")
    private String dias;

    @Column(name = "Detalles")
    private String detalles;

    @Column(name = "Creado_En", nullable = false)
    private Timestamp creadoEn;

    @Column(name = "Actualizado_En")
    private Timestamp actualizadoEn;

    /*@PrePersist
    protected void onCreate() {
        this.creadoEn = new Timestamp(System.currentTimeMillis());
    }*/

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ComidaRutinaIA> comidaRutinaIAS;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ModificacionRutinaChatbot> modificacionRutinaChatbots;
}
