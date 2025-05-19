package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.EstadisticasNutricionales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadisticasNutricionalesRepository extends JpaRepository<EstadisticasNutricionales, Long> {
    // Calcular estadísticas diarias por usuario
    @Query(value = "SELECT totalCalorias, totalProteinas, totalCarbohidratos, totalGrasas, totalAzucares, totalFibra " +
            "FROM EstadisticasNutricionales WHERE idUsuario = :idUsuario AND fecha = :fecha", nativeQuery = true)
    EstadisticasNutricionales calcularEstadisticasDiarias(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);

    // Obtener progreso semanal
    @Query(value = "SELECT * FROM EstadisticasNutricionales WHERE idUsuario = :idUsuario AND fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha", nativeQuery = true)
    List<EstadisticasNutricionales> obtenerProgresoSemanal(@Param("idUsuario") Integer idUsuario,
                                                           @Param("fechaInicio") String fechaInicio,
                                                           @Param("fechaFin") String fechaFin);

    // Calcular IMC del usuario por día
    @Query(value = "SELECT fecha, imc FROM EstadisticasNutricionales WHERE idUsuario = :idUsuario AND fecha = :fecha", nativeQuery = true)
    Float calcularIMC(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);

    // Obtener total de comidas registradas
    @Query(value = "SELECT totalComidas FROM EstadisticasNutricionales WHERE idUsuario = :idUsuario AND fecha = :fecha", nativeQuery = true)
    Integer obtenerTotalComidasRegistradas(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);
}
