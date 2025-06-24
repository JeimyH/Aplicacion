package com.example.Proyecto.DTO;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.RegistroAlimento;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroAlimentoSalidaDTO {

    private Long idRegistroAlimento;
    private Alimento alimento;
    private Float tamanoPorcion;
    private String unidadMedida;
    private String momentoDelDia;
    private String consumidoEn;

    public RegistroAlimentoSalidaDTO(RegistroAlimento registro) {
        this.idRegistroAlimento = registro.getIdRegistroAlimento();
        this.alimento = registro.getAlimento();
        this.tamanoPorcion = registro.getTamanoPorcion();
        this.unidadMedida = registro.getUnidadMedida();
        this.momentoDelDia = registro.getMomentoDelDia();
        this.consumidoEn = registro.getConsumidoEn().toString(); // ISO format
    }
}
