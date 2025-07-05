package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.AlimentoRecienteDTO;
import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.AlimentoReciente;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.AlimentoRecienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlimentoRecienteService {
    @Autowired
    public AlimentoRecienteRepository alimentoRecienteRepository;

    public void registrarAlimentoReciente(Long idUsuario, Alimento alimento, Usuario usuario) {
        // Eliminar si ya existe
        alimentoRecienteRepository.deleteByUsuario_IdUsuarioAndAlimento_IdAlimento(idUsuario, alimento.getIdAlimento());

        // Registrar nuevo
        AlimentoReciente reciente = new AlimentoReciente();
        reciente.setUsuario(usuario);
        reciente.setAlimento(alimento);
        reciente.setConsultadoEn(LocalDateTime.now());

        alimentoRecienteRepository.save(reciente);
    }

    public List<AlimentoRecienteDTO> obtenerRecientesPorUsuario(Long idUsuario) {
        List<AlimentoReciente> recientes = alimentoRecienteRepository.findByUsuario_IdUsuarioOrderByConsultadoEnDesc(idUsuario);

        return recientes.stream()
                .limit(5)
                .map(reciente -> new AlimentoRecienteDTO(
                        reciente.getIdReciente(),
                        reciente.getConsultadoEn().toString(),
                        reciente.getAlimento()
                ))
                .collect(Collectors.toList());
    }

    public void eliminarTodosPorUsuario(Long idUsuario) {
        alimentoRecienteRepository.deleteByUsuario_IdUsuario(idUsuario);
    }

    public void eliminarRecienteIndividual(Long idUsuario, Long idAlimento) {
        alimentoRecienteRepository.deleteByUsuario_IdUsuarioAndAlimento_IdAlimento(idUsuario, idAlimento);
    }

}
