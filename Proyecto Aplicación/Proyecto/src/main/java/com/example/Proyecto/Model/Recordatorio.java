package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Recordatorio")
public class Recordatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recordatorio")
    private Long idRecordatorio;

    @Column(name = "Origen")
    @Enumerated(EnumType.STRING)
    private TipoRecordatorio tipoRecordatorio;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "Hora")
    private Time hora;

    @Column(name = "Activo")
    private boolean activo;

    public enum TipoRecordatorio{
        Agua,
        Comida,
        Actividad,
        Personalizado
    }

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
}
