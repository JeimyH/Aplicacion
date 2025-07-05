package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.NutrientesTotalesDTO;
import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.EstadisticasNutricionales;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Model.UnidadEquivalencia;
import com.example.Proyecto.Repository.EstadisticasNutricionalesRepository;
import com.example.Proyecto.Repository.RegistroAlimentoRepository;
import com.example.Proyecto.Repository.UnidadEquivalenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EstadisticasNutricionalesService {
    @Autowired
    public EstadisticasNutricionalesRepository estadisticasNutricionalesRepository;

    @Autowired
    public RegistroAlimentoRepository registroAlimentoRepository;

    @Autowired
    public UnidadEquivalenciaRepository unidadEquivalenciaRepository;

    public List<EstadisticasNutricionales> listarEstadisticasNutricionales(){
        // Validacion para intentar obtener la lista de Estadisticas Nutricionales
        try {
            List<EstadisticasNutricionales> estadisticasNutricionales = estadisticasNutricionalesRepository.findAll();
            // Validar que la lista no sea nula
            if (estadisticasNutricionales == null) {
                throw new IllegalStateException("No se encontraron Estadisticas Nutricionales.");
            }
            return estadisticasNutricionales;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar las Estadisticas Nutricionales: " + e.getMessage(), e);
        }
    }

    public Optional<EstadisticasNutricionales> listarPorIdEstadisticasNutricionales(long idEstadistica){
        try {
            Optional<EstadisticasNutricionales> estadisticasNutricionales = estadisticasNutricionalesRepository.findById(idEstadistica);
            if (estadisticasNutricionales.isPresent()) {
                return estadisticasNutricionales;
            } else {
                throw new IllegalStateException("No se encontraron Estadisticas Nutricionales.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la Estadistica Nutricional " + idEstadistica +": "+ e.getMessage(), e);
        }
    }

    public EstadisticasNutricionales guardarEstadisticasNutricionales(EstadisticasNutricionales estadisticasNutricionales){
        try{
            if(estadisticasNutricionales==null){
                throw new IllegalArgumentException("La Estadistica Nutricional no puede ser nula");

            }else{
                if (estadisticasNutricionales.getFecha() == null) {
                    throw new IllegalArgumentException("La fecha de la Estadistica Nutricional es obligatoria.");
                }else if(estadisticasNutricionales.getTotalCalorias() == 0){
                    throw new IllegalArgumentException("El total de las calorias es obligatorio.");
                } else if (estadisticasNutricionales.getImc() == 0) {
                    throw new IllegalArgumentException("El imc es obligatorio.");
                }
                return  estadisticasNutricionalesRepository.save(estadisticasNutricionales);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la Estadistica Nutricional " + e.getMessage(), e);
        }
    }

    public void eliminarEstadisticasNutricionales(long idEstadistica){
        try {
            if (idEstadistica<=0) {
                throw new IllegalArgumentException("El ID de la Estadistica Nutricional debe ser un número positivo.");
            }
            if (!estadisticasNutricionalesRepository.existsById(idEstadistica)) {
                throw new NoSuchElementException("No se encontró una Estadistica Nutricional con el ID: " + idEstadistica);
            }
            estadisticasNutricionalesRepository.deleteById(idEstadistica);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la Estadistica Nutricional "+ idEstadistica +": "+ e.getMessage(), e);
        }
    }

    public EstadisticasNutricionales actualizarEstadisticasNutricionales(long idEstadistica, EstadisticasNutricionales estadisticaActualizado){
        Optional<EstadisticasNutricionales> estadisticasOpt = estadisticasNutricionalesRepository.findById(idEstadistica);
        if(estadisticasOpt.isPresent()){
            EstadisticasNutricionales estadisticaExistente = estadisticasOpt.get();
            estadisticaExistente.setFecha(estadisticaActualizado.getFecha());
            estadisticaExistente.setTotalCalorias(estadisticaActualizado.getTotalCalorias());
            estadisticaExistente.setTotalProteinas(estadisticaActualizado.getTotalProteinas());
            estadisticaExistente.setTotalCarbohidratos(estadisticaActualizado.getTotalCarbohidratos());
            estadisticaExistente.setTotalGrasas(estadisticaActualizado.getTotalGrasas());
            estadisticaExistente.setTotalAzucares(estadisticaActualizado.getTotalAzucares());
            estadisticaExistente.setTotalFibra(estadisticaActualizado.getTotalFibra());
            estadisticaExistente.setTotalSodio(estadisticaActualizado.getTotalSodio());
            estadisticaExistente.setTotalGrasasSaturadas(estadisticaActualizado.getTotalGrasasSaturadas());
            estadisticaExistente.setTotalAgua(estadisticaActualizado.getTotalAgua());
            estadisticaExistente.setTotalComidas(estadisticaActualizado.getTotalComidas());
            estadisticaExistente.setCaloriasDesayuno(estadisticaActualizado.getCaloriasDesayuno());
            estadisticaExistente.setCaloriasAlmuerzo(estadisticaActualizado.getCaloriasAlmuerzo());
            estadisticaExistente.setCaloriasCena(estadisticaActualizado.getCaloriasCena());
            estadisticaExistente.setCaloriasSnack(estadisticaActualizado.getCaloriasSnack());
            return estadisticasNutricionalesRepository.save(estadisticaExistente);
        }else{
            return null;
        }
    }

    public EstadisticasNutricionales obtenerEstadisticasDiarias(@Param("idUsuario") Long idUsuario, @Param("fecha") String fecha){
        return estadisticasNutricionalesRepository.calcularEstadisticasDiarias(idUsuario,fecha);
    }

    public List<EstadisticasNutricionales> obtenerProgresosSemanales(@Param("idUsuario") Long idUsuario,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin){
        return estadisticasNutricionalesRepository.obtenerProgresoSemanal(idUsuario,fechaInicio,fechaFin);
    }

    public Float obtenerIMC(@Param("idUsuario") Long idUsuario, @Param("fecha") String fecha){
        return estadisticasNutricionalesRepository.calcularIMC(idUsuario,fecha);
    }

    public Integer totalComidasRegistradas(@Param("idUsuario") Long idUsuario, @Param("fecha") String fecha){
        return estadisticasNutricionalesRepository.obtenerTotalComidasRegistradas(idUsuario,fecha);
    }

    public NutrientesTotalesDTO obtenerTotalesPorFecha(Long idUsuario, LocalDate fecha) {
        NutrientesTotalesDTO totales = new NutrientesTotalesDTO();

        // 1. Obtener todos los registros del usuario para esa fecha
        List<RegistroAlimento> registros = registroAlimentoRepository.findByUsuarioAndFecha(
                idUsuario,
                fecha.atStartOfDay(),
                fecha.atTime(LocalTime.MAX)
        );

        if (registros.isEmpty()) {
            System.out.println("🔍 No se encontraron registros para el usuario " + idUsuario + " el " + fecha);
        }

        for (RegistroAlimento registro : registros) {
            Alimento alimento = registro.getAlimento();
            if (alimento == null) {
                System.out.println("⚠️ Registro sin alimento asociado. ID Registro: " + registro.getIdRegistroAlimento());
                continue;
            }

            Float cantidadBase = alimento.getCantidadBase();
            String unidadBase = alimento.getUnidadBase();
            Float tamanoPorcion = registro.getTamanoPorcion();
            String unidadMedida = registro.getUnidadMedida();

            if (cantidadBase == null || unidadBase == null || tamanoPorcion == null || unidadMedida == null) {
                System.out.println("⚠️ Datos incompletos en registro con ID " + registro.getIdRegistroAlimento());
                continue;
            }

            // 2. Calcular el factor de conversión
            float factor = 1f;
            if (!unidadBase.equalsIgnoreCase(unidadMedida)) {
                Optional<UnidadEquivalencia> equivalenciaOpt =
                        unidadEquivalenciaRepository.findByAlimentoAndUnidadOrigenAndUnidadDestino(
                                alimento, unidadMedida.toLowerCase(), unidadBase.toLowerCase());

                if (equivalenciaOpt.isPresent()) {
                    factor = equivalenciaOpt.get().getFactorConversion();
                } else {
                    System.out.println("❌ No se encontró equivalencia para el alimento " +
                            alimento.getNombreAlimento() + " de " + unidadMedida + " a " + unidadBase +
                            " (registro ID " + registro.getIdRegistroAlimento() + ")");
                    continue;
                }
            }

            float proporcion = (tamanoPorcion * factor) / cantidadBase;
            if (proporcion <= 0) {
                System.out.println("❌ Proporción inválida para el alimento " + alimento.getNombreAlimento());
                continue;
            }

            // 3. Sumar nutrientes
            System.out.println("✅ Procesando alimento: " + alimento.getNombreAlimento() +
                    " | Tam: " + tamanoPorcion + " " + unidadMedida +
                    " | Factor: " + factor +
                    " | Base: " + cantidadBase + " " + unidadBase +
                    " | Proporción: " + proporcion);

            totales.setCalorias(totales.getCalorias() + alimento.getCalorias() * proporcion);
            totales.setProteinas(totales.getProteinas() + alimento.getProteinas() * proporcion);
            totales.setCarbohidratos(totales.getCarbohidratos() + alimento.getCarbohidratos() * proporcion);
            totales.setGrasas(totales.getGrasas() + alimento.getGrasas() * proporcion);
            totales.setAzucares(totales.getAzucares() + alimento.getAzucares() * proporcion);
            totales.setFibra(totales.getFibra() + alimento.getFibra() * proporcion);
            totales.setSodio(totales.getSodio() + alimento.getSodio() * proporcion);
            totales.setGrasasSaturadas(totales.getGrasasSaturadas() + alimento.getGrasasSaturadas() * proporcion);
        }

        return totales;
    }
}
