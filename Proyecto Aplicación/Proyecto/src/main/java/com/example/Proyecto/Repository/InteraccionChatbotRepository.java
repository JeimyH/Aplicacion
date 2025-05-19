package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.InteraccionChatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteraccionChatbotRepository extends JpaRepository<InteraccionChatbot, Long> {
    // Registrar interacción del chatbot
    @Modifying
    @Query(value = "INSERT INTO InteraccionChatbot (idSesion, mensajeUsuario, respuestaChatbot, tipoConsulta, fechaRegistro) VALUES (:idSesion, :mensajeUsuario, :respuestaChatbot, :tipoConsulta, CURRENT_TIMESTAMP)", nativeQuery = true)
    void registrarInteraccion(@Param("idSesion") Integer idSesion,
                              @Param("mensajeUsuario") String mensajeUsuario,
                              @Param("respuestaChatbot") String respuestaChatbot,
                              @Param("tipoConsulta") String tipoConsulta);

    // Obtener historial de interacciones
    @Query(value = "SELECT * FROM InteraccionChatbot WHERE idSesion = :idSesion ORDER BY fechaRegistro DESC", nativeQuery = true)
    List<InteraccionChatbot> obtenerHistorialInteracciones(@Param("idSesion") Integer idSesion);

    // Consultar respuesta según tipo de consulta
    @Query(value = "SELECT respuestaChatbot FROM InteraccionChatbot WHERE idSesion = :idSesion AND tipoConsulta = :tipoConsulta ORDER BY fechaRegistro DESC LIMIT 1", nativeQuery = true)
    String consultarRespuestaPorTipo(@Param("idSesion") Integer idSesion, @Param("tipoConsulta") String tipoConsulta);

    // Filtrar por fecha y tipo de respuesta
    @Query(value = "SELECT * FROM InteraccionChatbot WHERE idSesion = :idSesion AND fechaRegistro BETWEEN :fechaInicio AND :fechaFin AND tipoConsulta = :tipoConsulta", nativeQuery = true)
    List<InteraccionChatbot> filtrarPorFechaYTipo(@Param("idSesion") Integer idSesion,
                                                  @Param("fechaInicio") String fechaInicio,
                                                  @Param("fechaFin") String fechaFin,
                                                  @Param("tipoConsulta") String tipoConsulta);
}
