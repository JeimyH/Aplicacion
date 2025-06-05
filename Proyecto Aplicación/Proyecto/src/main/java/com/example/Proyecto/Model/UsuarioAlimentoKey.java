package com.example.Proyecto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAlimentoKey implements Serializable {
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_alimento")
    private Long idAlimento;
}
