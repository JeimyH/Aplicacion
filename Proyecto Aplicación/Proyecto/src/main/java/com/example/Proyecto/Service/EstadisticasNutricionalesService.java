package com.example.Proyecto.Service;

import com.example.Proyecto.Model.EstadisticasNutricionales;
import com.example.Proyecto.Repository.EstadisticasNutricionalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EstadisticasNutricionalesService {
    @Autowired
    public EstadisticasNutricionalesRepository estadisticasNutricionalesRepository;

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

    public Optional<EstadisticasNutricionales> listarPorIdEstadisticasNutricionales(long id_estadistica){
        try {
            Optional<EstadisticasNutricionales> estadisticasNutricionales = estadisticasNutricionalesRepository.findById(id_estadistica);
            if (estadisticasNutricionales.isPresent()) {
                return estadisticasNutricionales;
            } else {
                throw new IllegalStateException("No se encontraron Estadisticas Nutricionales.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la Estadistica Nutricional " + id_estadistica +": "+ e.getMessage(), e);
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

    public void eliminarEstadisticasNutricionales(long id_estadistica){
        try {
            if (id_estadistica<=0) {
                throw new IllegalArgumentException("El ID de la Estadistica Nutricional debe ser un número positivo.");
            }
            if (!estadisticasNutricionalesRepository.existsById(id_estadistica)) {
                throw new NoSuchElementException("No se encontró una Estadistica Nutricional con el ID: " + id_estadistica);
            }
            estadisticasNutricionalesRepository.deleteById(id_estadistica);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la Estadistica Nutricional "+ id_estadistica +": "+ e.getMessage(), e);
        }
    }

    public EstadisticasNutricionales actualizarEstadisticasNutricionales(long id_estadistica, EstadisticasNutricionales estadisticaActualizado){
        Optional<EstadisticasNutricionales> estadisticasOpt = estadisticasNutricionalesRepository.findById(id_estadistica);
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

    public EstadisticasNutricionales obtenerEstadisticasDiarias(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return estadisticasNutricionalesRepository.calcularEstadisticasDiarias(id_usuario,fecha);
    }

    public List<EstadisticasNutricionales> obtenerProgresosSemanales(@Param("id_usuario") Integer id_usuario,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin){
        return estadisticasNutricionalesRepository.obtenerProgresoSemanal(id_usuario,fechaInicio,fechaFin);
    }

    public Float obtenerIMC(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return estadisticasNutricionalesRepository.calcularIMC(id_usuario,fecha);
    }

    public Integer totalComidasRegistradas(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return estadisticasNutricionalesRepository.obtenerTotalComidasRegistradas(id_usuario,fecha);
    }

}
