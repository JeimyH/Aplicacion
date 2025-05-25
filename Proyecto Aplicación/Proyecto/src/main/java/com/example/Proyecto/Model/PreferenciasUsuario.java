package com.example.Proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Preferencias_Usuario")
public class PreferenciasUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_preferencia")
    private Long idPreferencia;

    @Column(name = "Objetivo_Agua_Diario")
    private Float objetivoAguaDiario;

    @Column(name = "Comidas_Preferidas")
    private String comidasPreferidas;

    @Column(name = "Alimentos_Excluidos")
    private String alimentosExcluidos;

    @Column(name = "Configuraciones_Notificaciones")
    private String configuracionesNotificaciones;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

}


