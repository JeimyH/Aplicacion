package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Sesion_Chatbot")
public class SesionChatbot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_sesion;

    @Column(name = "Inicio_Sesion", nullable = false)
    private Timestamp inicioSesion;

    @Column(name = "Fin_Sesion")
    private Timestamp finSesion;

    @Column(name = "Mensajes")
    private String mensajes;

    @Column(name = "Retroalimentacion")
    private String retroalimentacion;

    // Relaciones

    @OneToMany(mappedBy = "sesionChatbot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InteraccionChatbot> interaccionChatbots;

    @OneToMany(mappedBy = "sesionChatbot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegistroRespuestasIA> registroRespuestasIAS;

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
}
