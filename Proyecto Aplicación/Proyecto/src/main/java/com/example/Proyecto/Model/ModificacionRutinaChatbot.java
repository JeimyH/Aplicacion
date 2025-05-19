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
@Table(name = "Modificacion_Rutina_Chatbot")
public class ModificacionRutinaChatbot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_modificacion;

    @Column(name = "Fecha", nullable = false)
    private Timestamp fecha;

    @Column(name = "Accion", nullable = false)
    private Accion accion;

    @Column(name = "Comida", nullable = false)
    private String comida;

    @Lob
    @Column(name = "Motivo", columnDefinition = "TEXT")
    private String motivo;

    public enum Accion{
        Agregar,
        Eliminar,
        Cambiar
    }

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="id_rutina", nullable = false)
    @JsonIgnore
    private RutinaAlimenticiaIA rutina;
}
