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
@Table(name = "Interaccion_Chatbot")
public class InteraccionChatbot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interaccion")
    private Long idInteraccion;

    @Column(name = "Consulta_Usuario", nullable = false)
    private String consultaUsuario;

    @Column(name = "Respuesta_IA", nullable = false)
    private String respuestaIA;

    @Column(name = "Tipo_Intento")
    @Enumerated(EnumType.STRING)
    private TipoIntento tipoIntento;

    @Column(name = "Tipo_Accion")
    @Enumerated(EnumType.STRING)
    private TipoAccion tipoAccion;

    @Column(name = "Tema")
    private String tema;

    @Column(name = "Timestamp", nullable = false)
    private Timestamp timestamp;

    public enum TipoIntento{
        Modificar_Rutina,
        Pregunta_Nutricional,
        Otros
    }

    public enum TipoAccion{
        Modificar,
        Agregar,
        Eliminar
    }

    //Relaciones

    @ManyToOne
    @JoinColumn(name="id_sesion", nullable = false)
    @JsonIgnore
    private SesionChatbot sesionChatbot;
}
