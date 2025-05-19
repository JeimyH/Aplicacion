package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RutinaAlimenticiaIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaAlimenticiaIARepository extends JpaRepository<RutinaAlimenticiaIA, Long> {
    // Obtener rutina alimenticia por usuario y fecha
    @Query(value = "SELECT * FROM RutinaAlimenticiaIA WHERE idUsuario = :idUsuario AND fechaInicio <= :fecha AND fechaFin >= :fecha", nativeQuery = true)
    RutinaAlimenticiaIA obtenerRutinaPorUsuarioYFecha(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);

    // Crear nueva rutina
    @Modifying
    @Query(value = "INSERT INTO RutinaAlimenticiaIA (idUsuario, fechaInicio, fechaFin, objetivoCaloricoDiario, detalles, createdAt, actualizado) VALUES (:idUsuario, :fechaInicio, :fechaFin, :objetivoCaloricoDiario, :detalles, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
    void crearNuevaRutina(@Param("idUsuario") Integer idUsuario,
                          @Param("fechaInicio") String fechaInicio,
                          @Param("fechaFin") String fechaFin,
                          @Param("objetivoCaloricoDiario") Float objetivoCaloricoDiario,
                          @Param("detalles") String detalles);

    // Actualizar rutina
    @Modifying
    @Query(value = "UPDATE RutinaAlimenticiaIA SET fechaInicio = :fechaInicio, fechaFin = :fechaFin, objetivoCaloricoDiario = :objetivoCaloricoDiario, detalles = :detalles, actualizado = CURRENT_TIMESTAMP WHERE idRutina = :idRutina", nativeQuery = true)
    void actualizarRutina(@Param("idRutina") Integer idRutina,
                          @Param("fechaInicio") String fechaInicio,
                          @Param("fechaFin") String fechaFin,
                          @Param("objetivoCaloricoDiario") Float objetivoCaloricoDiario,
                          @Param("detalles") String detalles);

    // Consultar detalles de rutina semanal
    @Query(value = "SELECT * FROM RutinaAlimenticiaIA WHERE idUsuario = :idUsuario AND fechaInicio <= :fechaFin AND fechaFin >= :fechaInicio", nativeQuery = true)
    List<RutinaAlimenticiaIA> consultarDetallesRutinaSemanal(@Param("idUsuario") Integer idUsuario,
                                                             @Param("fechaInicio") String fechaInicio,
                                                             @Param("fechaFin") String fechaFin);

    // Obtener rutina por día específico
    @Query(value = "SELECT * FROM RutinaAlimenticiaIA WHERE idUsuario = :idUsuario AND fechaInicio <= :fecha AND fechaFin >= :fecha", nativeQuery = true)
    RutinaAlimenticiaIA obtenerRutinaPorDiaEspecifico(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);
}
