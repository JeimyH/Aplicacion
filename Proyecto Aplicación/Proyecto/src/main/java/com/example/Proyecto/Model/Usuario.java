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
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "Correo", nullable = false, length = 100)
    private String correo;

    @Column(name = "Contraseña", nullable = false, length = 150)
    private String contrasena;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Fecha_Nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "Altura", nullable = false)
    private Float altura;

    @Column(name = "Peso", nullable = false)
    private Float peso;

    @Column(name = "Sexo", nullable = false, length = 25)
    private String sexo;

    @Column(name = "Restricciones_Dieta")
    private String restriccionesDieta;

    @Column(name = "Objetivos_Salud")
    private String objetivosSalud;

    @Column(name = "Peso_Objetivo")
    private Float pesoObjetivo;

    @Column(name = "Creado_En", nullable = false)
    private Timestamp creadoEn;

    @Column(name = "Actualizado_En")
    private Timestamp actualizadoEn;

    /*@PrePersist
    protected void onCreate() {
        this.creadoEn = new Timestamp(System.currentTimeMillis());
    }*/

    // Relaciones entre tablas
    // Uno a muchos

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegistroAgua> registroAguas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegistroAlimento> registroAlimentos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RutinaAlimenticiaIA> rutinaAlimenticiaIAS;

    /*
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Alimento> alimentos;
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EstadisticasNutricionales> estadisticasNutricionales;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegistroRespuestasIA> registroRespuestasIAS;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SesionChatbot> sesionChatbots;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TokenSesion> tokenSesions;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Recordatorio> recordatorios;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ModificacionRutinaChatbot> modificacionRutinaChatbots;

    // Uno a Uno
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private PreferenciasUsuario preferenciasUsuario;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private ConfiguracionAplicacion configuracionAplicacion;

}
