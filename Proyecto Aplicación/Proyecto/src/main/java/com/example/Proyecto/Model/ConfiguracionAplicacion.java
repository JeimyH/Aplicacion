package com.example.Proyecto.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Configuracion_Apilcacion")
public class ConfiguracionAplicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_configuracion;

    @Column(name = "Idioma")
    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (más legible)
    private Idioma idioma;

    @Column(name = "Notificaciones")
    private boolean notificaciones;

    @Column(name = "Tema")
    @Enumerated(EnumType.STRING)
    private Tema tema;

    @Column(name = "Frecuencia_Actualizaciones")
    @Enumerated(EnumType.STRING)
    private FrecuenciaActualizacion frecuenciaActualizacion;

    @Column(name = "Creado_En")
    private Timestamp creadoEn;

    public enum Idioma {
        Espanol,
        Ingles
    }

    public enum Tema{
        Claro,
        Oscuro
    }

    public enum FrecuenciaActualizacion{
        Diaria,
        Semanal,
        Mensual
    }

    //Relacion a la tabla usuario
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

}


