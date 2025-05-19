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
@Table(name = "Registro_Respuestas_IA")
public class RegistroRespuestasIA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_respuesta;

    @Column(name = "Datos_Respuesta", nullable = false, length = 100)
    private String datosRespuesta;

    @Column(name = "Creado_En", nullable = false)
    private Timestamp creadoEn;

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_sesion", nullable = false)
    @JsonIgnore
    private SesionChatbot sesionChatbot;

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
}
