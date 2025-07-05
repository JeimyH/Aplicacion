package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.ActividadDiaDTO;
import com.example.Proyecto.Repository.RegistroAguaRepository;
import com.example.Proyecto.Repository.RegistroAlimentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActividadService {
    private final RegistroAguaRepository registroAguaRepository;
    private final RegistroAlimentoRepository registroAlimentoRepository;

    public ActividadService(RegistroAguaRepository aguaRepo, RegistroAlimentoRepository alimentoRepo) {
        this.registroAguaRepository = aguaRepo;
        this.registroAlimentoRepository = alimentoRepo;
    }

    public List<ActividadDiaDTO> obtenerDiasConActividad(Long idUsuario) {
        List<LocalDate> fechasAgua = registroAguaRepository.findFechasAguaPorUsuario(idUsuario);
        List<java.sql.Date> fechasComidaSql = registroAlimentoRepository.findFechasComidaPorUsuario(idUsuario);

        List<LocalDate> fechasComida = fechasComidaSql.stream()
                .map(java.sql.Date::toLocalDate)
                .collect(Collectors.toList());

        Set<LocalDate> todasLasFechas = new HashSet<>();
        todasLasFechas.addAll(fechasAgua);
        todasLasFechas.addAll(fechasComida);

        List<ActividadDiaDTO> resultado = new ArrayList<>();

        for (LocalDate fecha : todasLasFechas) {
            boolean tieneAgua = fechasAgua.contains(fecha);
            boolean tieneComida = fechasComida.contains(fecha);

            String tipo = (tieneAgua && tieneComida) ? "AMBOS" : (tieneAgua ? "AGUA" : "COMIDA");

            resultado.add(new ActividadDiaDTO(fecha, tipo));
        }

        return resultado;
    }

}

