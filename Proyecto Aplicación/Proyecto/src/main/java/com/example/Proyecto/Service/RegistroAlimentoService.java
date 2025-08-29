package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.RegistroAlimentoEntradaDTO;
import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Model.UnidadEquivalencia;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.AlimentoRepository;
import com.example.Proyecto.Repository.RegistroAlimentoRepository;
import com.example.Proyecto.Repository.UnidadEquivalenciaRepository;
import com.example.Proyecto.Repository.UsuarioRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;


@Service
public class RegistroAlimentoService {

    private static final Logger log = LoggerFactory.getLogger(RegistroAlimentoService.class);

    @Autowired
    public RegistroAlimentoRepository registroAlimentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public AlimentoRepository alimentoRepository;

    @Autowired
    public UnidadEquivalenciaRepository unidadEquivalenciaRepository;

    @Autowired
    public EstadisticasNutricionalesService estadisticasService;

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

    @Transactional
    public RegistroAlimento guardarRegistro(RegistroAlimentoEntradaDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Alimento alimento = alimentoRepository.findById(dto.getIdAlimento())
                .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));

        String unidadOrigen = dto.getUnidadOriginal().toLowerCase();  // <- original
        String unidadDestino = "gramos";
        float cantidadOriginal = dto.getTamanoOriginal();

        // Validación explícita de unidad original (evita valores inválidos)
        List<String> unidadesValidas = List.of(
                "mg", "g", "kg", "ml", "l", "tsp", "tbsp", "cup", "oz", "lb",
                "unidad", "porción", "rebanada", "pieza", "taza", "vaso",
                "lonja", "filete", "puñado", "cucharada"
        );

        if (!unidadesValidas.contains(unidadOrigen)) {
            throw new IllegalArgumentException("Unidad original inválida: " + unidadOrigen);
        }

        float cantidadEnGramos;

        Optional<UnidadEquivalencia> equivalenciaOpt = unidadEquivalenciaRepository
                .findByAlimentoAndUnidadOrigenAndUnidadDestino(alimento, unidadOrigen, unidadDestino);

        if (equivalenciaOpt.isPresent()) {
            float factor = equivalenciaOpt.get().getFactorConversion();
            cantidadEnGramos = cantidadOriginal * factor;
        } else {
            float factorEstimado = 1.0f;
            UnidadEquivalencia nuevaEquivalencia = new UnidadEquivalencia();
            nuevaEquivalencia.setAlimento(alimento);
            nuevaEquivalencia.setUnidadOrigen(unidadOrigen);
            nuevaEquivalencia.setUnidadDestino(unidadDestino);
            nuevaEquivalencia.setFactorConversion(factorEstimado);
            unidadEquivalenciaRepository.save(nuevaEquivalencia);

            cantidadEnGramos = cantidadOriginal * factorEstimado;
        }

        RegistroAlimento registro = new RegistroAlimento();
        registro.setUsuario(usuario);
        registro.setAlimento(alimento);
        registro.setTamanoPorcion(cantidadEnGramos);      // convertido
        registro.setUnidadMedida(unidadDestino);          // convertido (gramos)
        registro.setTamanoOriginal(cantidadOriginal);     // original
        registro.setUnidadOriginal(unidadOrigen);         // original
        registro.setMomentoDelDia(dto.getMomentoDelDia());
        registro.setConsumidoEn(LocalDateTime.now());

        RegistroAlimento registroGuardado = registroAlimentoRepository.save(registro);
        // Actualizar estadísticas diarias justo después de guardar el registro
        LocalDate fechaRegistro = registroGuardado.getConsumidoEn().toLocalDate();
        estadisticasService.guardarEstadisticaDiaria(usuario.getIdUsuario(), fechaRegistro);

        // Actualizar estadísticas mensuales
        int anio = fechaRegistro.getYear();
        int mes = fechaRegistro.getMonthValue();
        estadisticasService.guardarEstadisticaMensual(usuario.getIdUsuario(), anio, mes);

        return registroGuardado;
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

    public void eliminarPorMomentoYFecha(Long idUsuario, String momento, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        registroAlimentoRepository.deleteByUsuarioFechaYMomento(idUsuario, momento, inicio, fin);
    }

    public void eliminarRegistroPorId(Long idRegistro) {
        if (!registroAlimentoRepository.existsById(idRegistro)) {
            throw new RuntimeException("Registro no encontrado");
        }
        registroAlimentoRepository.deleteById(idRegistro);
    }

    // Obtener todas las unidades de origen de un alimento por su ID.
    public List<String> obtenerUnidadesPorIdAlimento(Long idAlimento) {
        List<String> unidades = unidadEquivalenciaRepository.findUnidadOrigenByAlimentoId(idAlimento);
        log.info("Obteniendo unidades de origen para Alimento ID {}: {}", idAlimento, unidades);
        return unidades;
    }

    // Obtener todas las unidades de origen de un alimento por su nombre.
    public List<String> obtenerUnidadesPorNombreAlimento(String nombreAlimento) {
        List<String> unidades = unidadEquivalenciaRepository.findUnidadOrigenByAlimentoNombre(nombreAlimento);
        log.info("Obteniendo unidades de origen para Alimento '{}': {}", nombreAlimento, unidades);
        return unidades;
    }


}
