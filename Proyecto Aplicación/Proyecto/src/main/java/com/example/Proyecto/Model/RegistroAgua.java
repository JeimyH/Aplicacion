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
@Table(name = "Registro_Agua")
public class RegistroAgua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_registroAgua;

    @Column(name = "Cantidadml", nullable = false)
    private float cantidadml;

    @Column(name = "Registrado_En", nullable = false)
    private Timestamp registradoEn;

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
}
