package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.NutrientesConsumidosDTO;
import com.example.Proyecto.DTO.ResumenDiarioDTO;
import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Repository.AlimentoRepository;
import com.example.Proyecto.Repository.RegistroAlimentoRepository;
import com.example.Proyecto.Repository.UnidadEquivalenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegistroAlimentoService {
    @Autowired
    public RegistroAlimentoRepository registroAlimentoRepository;

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

    public Optional<RegistroAlimento> listarPorIdRegistroAlimento(long id_registroAlimento){
        try {
            Optional<RegistroAlimento> registroAlimento = registroAlimentoRepository.findById(id_registroAlimento);
            if (registroAlimento.isPresent()) {
                return registroAlimento;
            } else {
                throw new IllegalStateException("No se encontraron Registros de Alimentos.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el Registro del Alimento " + id_registroAlimento +": "+ e.getMessage(), e);
        }
    }

    public RegistroAlimento guardarRegistroAlimento(RegistroAlimento registroAlimento){
        // Inicializa el campo creadoEn
        registroAlimento.setConsumidoEn(new Timestamp(System.currentTimeMillis()));
        try{
            if(registroAlimento==null){
                throw new IllegalArgumentException("El Registro del Alimento no puede ser nulo");

            }else{
                if (registroAlimento.getTamanoPorcion() == 0) {
                    throw new IllegalArgumentException("El Tamaño de la porcion del Registro del Alimento es obligatorio.");
                }else if(registroAlimento.getConsumidoEn() == null){
                    throw new IllegalArgumentException("El timestamp de consumido en del Registro del Alimento es obligatorio.");
                }else if(registroAlimento.getUnidadMedida() == null){
                    throw new IllegalArgumentException("La unidad de medida del Registro del Alimento es obligatoria.");
                }else if(registroAlimento.getTamanoPorcion() <= 0){
                    throw new IllegalArgumentException("El tamaño de la porcion consumida del Alimento es obligatoria.");
                }
                return  registroAlimentoRepository.save(registroAlimento);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el Alimento" + e.getMessage(), e);
        }
    }

    public void eliminarRegistroAlimento(long id_registroAlimento){
        try {
            if (id_registroAlimento<=0) {
                throw new IllegalArgumentException("El ID del Registro del Alimento debe ser un número positivo.");
            }
            if (!registroAlimentoRepository.existsById(id_registroAlimento)) {
                throw new NoSuchElementException("No se encontró un Registro del Alimento con el ID: " + id_registroAlimento);
            }
            registroAlimentoRepository.deleteById(id_registroAlimento);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el Registro del Alimento "+ id_registroAlimento +": "+ e.getMessage(), e);
        }
    }

    public RegistroAlimento actualizarRegistroAlimento(long id_registroAlimento, RegistroAlimento registroAlimentoActualizado){
        Optional<RegistroAlimento> registroAlimentoOpt = registroAlimentoRepository.findById(id_registroAlimento);
        if(registroAlimentoOpt.isPresent()){
            RegistroAlimento registroAlimentoExistente = registroAlimentoOpt.get();
            registroAlimentoExistente.setTamanoPorcion(registroAlimentoActualizado.getTamanoPorcion());
            registroAlimentoExistente.setUnidadMedida(registroAlimentoActualizado.getUnidadMedida());
            registroAlimentoExistente.setTamanoPorcion(registroAlimentoActualizado.getTamanoPorcion());
            registroAlimentoExistente.setMomentoDelDia(registroAlimentoActualizado.getMomentoDelDia());
            registroAlimentoExistente.setFavorito(registroAlimentoActualizado.isFavorito());
            return registroAlimentoRepository.save(registroAlimentoExistente);
        }else{
            return null;
        }
    }
/*
    // Metodo de conversion de unidades
    public float convertirUnidad(float cantidad, String unidadOrigen, float cantidadBase, String unidadBase) {
        if (unidadOrigen.equalsIgnoreCase(unidadBase)) {
            return cantidad / cantidadBase;
        }

        return unidadEquivalenciaRepository
                .findByUnidadOrigenAndUnidadDestino(unidadOrigen, unidadBase)
                .map(eq -> (cantidad * eq.getFactorConversion()) / cantidadBase)
                .orElseThrow(() -> new RuntimeException("No se pudo convertir " + unidadOrigen + " a " + unidadBase));
    }

    // Metodo para obtener nutrientes por día
    public List<NutrientesConsumidosDTO> obtenerNutrientesPorUsuarioYFecha(Long idUsuario, LocalDate fecha) {
        Timestamp inicio = Timestamp.valueOf(fecha.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fecha.plusDays(1).atStartOfDay());

        List<RegistroAlimento> registros = registroAlimentoRepository.findRegistrosPorUsuarioYFecha(idUsuario, inicio, fin);

        List<NutrientesConsumidosDTO> resultado = new ArrayList<>();

        for (RegistroAlimento registro : registros) {
            Alimento alimento = registro.getAlimento();
            float factor = convertirUnidad(
                    registro.getTamanoPorcion(),
                    registro.getUnidadMedida(),
                    alimento.getCantidadBase(),
                    alimento.getUnidadBase()
            );

            NutrientesConsumidosDTO dto = new NutrientesConsumidosDTO();
            dto.setNombreAlimento(alimento.getNombreAlimento());
            dto.setCantidadConsumida(registro.getTamanoPorcion());
            dto.setUnidadMedida(registro.getUnidadMedida());
            dto.setCalorias(alimento.getCalorias() * factor);
            dto.setProteinas(alimento.getProteinas() * factor);
            dto.setCarbohidratos(alimento.getCarbohidratos() * factor);
            dto.setGrasas(alimento.getGrasas() * factor);
            dto.setAzucares(alimento.getAzucares() * factor);
            dto.setFibra(alimento.getFibra() * factor);
            dto.setSodio(alimento.getSodio() * factor);
            dto.setGrasasSaturadas(alimento.getGrasasSaturadas() * factor);

            resultado.add(dto);
        }

        return resultado;
    }

    // Metodo para obtener resumen diario
    public ResumenDiarioDTO calcularResumenPorFecha(Long idUsuario, LocalDate fecha) {
        Timestamp inicio = Timestamp.valueOf(fecha.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fecha.plusDays(1).atStartOfDay());

        List<RegistroAlimento> registros = registroAlimentoRepository.findRegistrosPorUsuarioYFecha(idUsuario, inicio, fin);

        float calorias = 0, proteinas = 0, carbohidratos = 0, grasas = 0,
                azucares = 0, fibra = 0, sodio = 0, grasasSaturadas = 0;

        for (RegistroAlimento registro : registros) {
            Alimento alimento = registro.getAlimento();
            float factor = convertirUnidad(
                    registro.getTamanoPorcion(),
                    registro.getUnidadMedida(),
                    alimento.getCantidadBase(),
                    alimento.getUnidadBase()
            );

            calorias += alimento.getCalorias() * factor;
            proteinas += alimento.getProteinas() * factor;
            carbohidratos += alimento.getCarbohidratos() * factor;
            grasas += alimento.getGrasas() * factor;
            azucares += alimento.getAzucares() * factor;
            fibra += alimento.getFibra() * factor;
            sodio += alimento.getSodio() * factor;
            grasasSaturadas += alimento.getGrasasSaturadas() * factor;
        }

        return new ResumenDiarioDTO(calorias, proteinas, carbohidratos, grasas,
                azucares, fibra, sodio, grasasSaturadas);
    }


    public void obtenerAlimentoConsumido(@Param("id_usuario") Integer id_usuario, @Param("id_alimento") Integer id_alimento, @Param("tamanoPorcion") Float tamanoPorcion, @Param("consumidoEn") String consumidoEn){
        registroAlimentoRepository.registrarAlimentoConsumido(id_usuario,id_alimento,tamanoPorcion,consumidoEn);
    }

    public List<RegistroAlimento> obtenerHistorialPorFecha(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return registroAlimentoRepository.obtenerHistorialPorFecha(id_usuario, fecha);
    }

    public List<Object[]> obtenerTotalesDiarios(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return registroAlimentoRepository.calcularTotalesDiarios(id_usuario,fecha);
    }

    public void obtenerEliminarRegistroAlimento(@Param("id_registroAlimento") Integer id_registroAlimento){
        registroAlimentoRepository.eliminarRegistroAlimento(id_registroAlimento);
    }

 */
}
