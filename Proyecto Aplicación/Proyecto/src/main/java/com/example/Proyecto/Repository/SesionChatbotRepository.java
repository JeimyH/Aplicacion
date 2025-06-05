package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.InteraccionChatbot;
import com.example.Proyecto.Model.SesionChatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionChatbotRepository extends JpaRepository<SesionChatbot, Long> {
    // Obtener sesiones activas del usuario
    @Query(value = "SELECT * FROM SesionChatbot WHERE idUsuario = :idUsuario AND estado = 'ACTIVA'", nativeQuery = true)
    List<SesionChatbot> obtenerSesionesActivas(@Param("idUsuario") Integer idUsuario);

    // Crear nueva sesión
    @Modifying
    @Query(value = "INSERT INTO SesionChatbot (idUsuario, estado, fechaInicio) VALUES (:idUsuario, 'ACTIVA', CURRENT_TIMESTAMP)", nativeQuery = true)
    void crearNuevaSesion(@Param("idUsuario") Integer idUsuario);

    // Finalizar sesión
    @Modifying
    @Query(value = "UPDATE SesionChatbot SET estado = 'FINALIZADA', fechaFin = CURRENT_TIMESTAMP WHERE idSesion = :idSesion", nativeQuery = true)
    void finalizarSesion(@Param("idSesion") Integer idSesion);

    // Obtener mensajes y recomendaciones anteriores
    @Query(value = "SELECT * FROM InteraccionChatbot WHERE idSesion = :idSesion ORDER BY fechaRegistro DESC", nativeQuery = true)
    List<InteraccionChatbot> obtenerMensajesYRecomendaciones(@Param("idSesion") Integer idSesion);
}
