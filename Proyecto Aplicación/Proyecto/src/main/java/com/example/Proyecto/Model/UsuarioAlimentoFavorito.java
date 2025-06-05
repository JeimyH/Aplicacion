package com.example.Proyecto.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Usuario_Alimento_Favorito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAlimentoFavorito {

    @EmbeddedId
    private UsuarioAlimentoKey id = new UsuarioAlimentoKey();

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idAlimento")
    @JoinColumn(name = "id_alimento")
    private Alimento alimento;
}

