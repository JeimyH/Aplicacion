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
@Table(name = "Registro_Agua")
public class RegistroAgua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro_agua")
    private Long idRegistroAgua;

    @Column(name = "Cantidadml", nullable = false)
    private int cantidadml;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name="id_usuario", nullable = false)
    //@JsonIgnore
    private Usuario usuario;
}
