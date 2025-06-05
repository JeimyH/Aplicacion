package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.ModificacionRutinaChatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModificacionRutinaChatbotRepository extends JpaRepository<ModificacionRutinaChatbot, Long> {
    // Registrar modificación de rutina (agregar, eliminar, cambiar alimento)
    @Modifying
    @Query(value = "INSERT INTO ModificacionRespuestaChatbot (idSesion, tipoModificacion, descripcion, fechaRegistro) VALUES (:idSesion, :tipoModificacion, :descripcion, CURRENT_TIMESTAMP)", nativeQuery = true)
    void registrarModificacion(@Param("idSesion") Integer idSesion,
                               @Param("tipoModificacion") String tipoModificacion,
                               @Param("descripcion") String descripcion);

    // Obtener historial de cambios por fecha
    @Query(value = "SELECT * FROM ModificacionRespuestaChatbot WHERE fechaRegistro BETWEEN :fechaInicio AND :fechaFin ORDER BY fechaRegistro DESC", nativeQuery = true)
    List<ModificacionRutinaChatbot> obtenerHistorialPorFecha(@Param("fechaInicio") String fechaInicio,
                                                             @Param("fechaFin") String fechaFin);

    // Filtrar por tipo de modificación
    @Query(value = "SELECT * FROM ModificacionRespuestaChatbot WHERE tipoModificacion = :tipoModificacion ORDER BY fechaRegistro DESC", nativeQuery = true)
    List<ModificacionRutinaChatbot> filtrarPorTipoModificacion(@Param("tipoModificacion") String tipoModificacion);
}
