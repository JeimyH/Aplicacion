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
@Table(name = "Alimento")
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alimento")
    private Long idAlimento;

    @Column(name = "Nombre_Alimento", nullable = false, length = 100)
    private String nombreAlimento;

    @Column(name = "Calorias", nullable = false)
    private Float calorias;

    @Column(name = "Proteinas", nullable = false)
    private Float proteinas;

    @Column(name = "Carbohidratos", nullable = false)
    private Float carbohidratos;

    @Column(name = "Grasas", nullable = false)
    private Float grasas;

    @Column(name = "Azucares", nullable = false)
    private Float azucares;

    @Column(name = "Fibra", nullable = false)
    private Float fibra;

    @Column(name = "Sodio", nullable = false)
    private Float sodio;

    @Column(name = "Grasas_Saturadas", nullable = false)
    private Float grasasSaturadas;

    @Column(name = "Categoria", length = 50)
    private String categoria;

    @Column(name = "URL_Imagen")
    private String urlImagen;

    @Column(name = "Cantidad_Base", nullable = false)
    private Float cantidadBase;  // cuantos gramos equivale el consumo de este alimento

    @Column(name = "Unidad_Base", length =50, nullable = false)
    private String unidadBase;  // gramos

    //Relaciones

    @OneToMany(mappedBy = "alimento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegistroAlimento> registroAlimentos;

    @OneToMany(mappedBy = "alimento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UnidadEquivalencia> unidadEquivalencias;
}
