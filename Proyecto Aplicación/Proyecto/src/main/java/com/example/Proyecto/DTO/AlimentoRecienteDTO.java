package com.example.Proyecto.DTO;

import com.example.Proyecto.Model.Alimento;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlimentoRecienteDTO {
    private Long idReciente;
    private String consultadoEn;
    private Alimento alimento;

    public AlimentoRecienteDTO(Long idReciente, String consultadoEn, Alimento alimento) {
        this.idReciente = idReciente;
        this.consultadoEn = consultadoEn;
        this.alimento = alimento;
    }
}

