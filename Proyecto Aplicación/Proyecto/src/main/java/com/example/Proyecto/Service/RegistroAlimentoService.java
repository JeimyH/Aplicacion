package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.RegistroAlimentoEntradaDTO;
import com.example.Proyecto.DTO.RegistroPorFechaYMomentoDTO;
import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.AlimentoRepository;
import com.example.Proyecto.Repository.RegistroAlimentoRepository;
import com.example.Proyecto.Repository.UnidadEquivalenciaRepository;
import com.example.Proyecto.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistroAlimentoService {
    @Autowired
    public RegistroAlimentoRepository registroAlimentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public AlimentoRepository alimentoRepository;

    @Autowired
    public UnidadEquivalenciaRepository unidadEquivalenciaRepository;

    public List<RegistroAlimento> listarRegistroAlimento(){
        // Validacion para intentar obtener la lista de Registros de Alimento
        try {
            List<RegistroAlimento> registroAlimentos = registroAlimentoRepository.findAll();
            // Validar que la lista no sea nula
            if (registroAlimentos == null) {
                throw new IllegalStateException("No se encontraron Registros de Alimento.");
            }
            return registroAlimentos;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar los Registros de Alimento: " + e.getMessage(), e);
        }
    }

    public Optional<RegistroAlimento> listarPorIdRegistroAlimento(long idRegistroAlimento){
        try {
            Optional<RegistroAlimento> registroAlimento = registroAlimentoRepository.findById(idRegistroAlimento);
            if (registroAlimento.isPresent()) {
                return registroAlimento;
            } else {
                throw new IllegalStateException("No se encontraron Registros de Alimentos.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el Registro del Alimento " + idRegistroAlimento +": "+ e.getMessage(), e);
        }
    }

    public RegistroAlimento guardarRegistro(RegistroAlimentoEntradaDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Alimento alimento = alimentoRepository.findById(dto.getIdAlimento())
                .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));

        RegistroAlimento registro = new RegistroAlimento();
        registro.setUsuario(usuario);
        registro.setAlimento(alimento);
        registro.setTamanoPorcion(dto.getTamanoPorcion());
        registro.setUnidadMedida(dto.getUnidadMedida());
        registro.setMomentoDelDia(dto.getMomentoDelDia());
        registro.setConsumidoEn(LocalDateTime.now());
        // opcional, no se usa aquí

        return registroAlimentoRepository.save(registro);
    }

    public List<RegistroAlimento> obtenerRecientesPorUsuario(Long idUsuario) {
        return registroAlimentoRepository.findRecientesConAlimento(idUsuario);
    }



    public void eliminarRegistroAlimento(long idRegistroAlimento){
        try {
            if (idRegistroAlimento<=0) {
                throw new IllegalArgumentException("El ID del Registro del Alimento debe ser un número positivo.");
            }
            if (!registroAlimentoRepository.existsById(idRegistroAlimento)) {
                throw new NoSuchElementException("No se encontró un Registro del Alimento con el ID: " + idRegistroAlimento);
            }
            registroAlimentoRepository.deleteById(idRegistroAlimento);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el Registro del Alimento "+ idRegistroAlimento +": "+ e.getMessage(), e);
        }
    }

    public RegistroAlimento actualizarRegistroAlimento(long idRegistroAlimento, RegistroAlimento registroAlimentoActualizado){
        Optional<RegistroAlimento> registroAlimentoOpt = registroAlimentoRepository.findById(idRegistroAlimento);
        if(registroAlimentoOpt.isPresent()){
            RegistroAlimento registroAlimentoExistente = registroAlimentoOpt.get();
            registroAlimentoExistente.setTamanoPorcion(registroAlimentoActualizado.getTamanoPorcion());
            registroAlimentoExistente.setUnidadMedida(registroAlimentoActualizado.getUnidadMedida());
            registroAlimentoExistente.setMomentoDelDia(registroAlimentoActualizado.getMomentoDelDia());
            return registroAlimentoRepository.save(registroAlimentoExistente);
        }else{
            return null;
        }
    }

    public List<RegistroAlimento> obtenerPorUsuarioFechaYMomento(Long idUsuario, LocalDate fecha, String momento) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return registroAlimentoRepository.findByUsuarioFechaYMomento(idUsuario, inicio, fin, momento);
    }



}
