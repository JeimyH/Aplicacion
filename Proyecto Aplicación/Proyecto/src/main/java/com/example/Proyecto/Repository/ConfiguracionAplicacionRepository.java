package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.ConfiguracionAplicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfiguracionAplicacionRepository extends JpaRepository<ConfiguracionAplicacion, Long> {
    // Obtener configuraciones del usuario
    @Query(value = "SELECT * FROM ConfiguracionAplicacion WHERE idUsuario = :idUsuario", nativeQuery = true)
    List<ConfiguracionAplicacion> obtenerConfiguracionesDelUsuario(@Param("idUsuario") Long idUsuario);

    // Actualizar idioma o unidades
    @Modifying
    @Query(value = "UPDATE ConfiguracionAplicacion SET idioma = :idioma, unidades = :unidades WHERE idUsuario = :idUsuario", nativeQuery = true)
    void actualizarIdiomaOUnidades(@Param("idUsuario") Long idUsuario,
                                   @Param("idioma") String idioma,
                                   @Param("unidades") String unidades);

    // Activar/desactivar notificaciones
    @Modifying
    @Query(value = "UPDATE ConfiguracionAplicacion SET notificacionesActivadas = :activadas WHERE idUsuario = :idUsuario", nativeQuery = true)
    void activarDesactivarNotificaciones(@Param("idUsuario") Long idUsuario, @Param("activadas") boolean activadas);
}
