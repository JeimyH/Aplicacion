package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RegistroAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroAguaRepository extends JpaRepository<RegistroAgua, Long> {
    // Registrar cantidad de agua
    @Modifying
    @Query(value = "INSERT INTO RegistroAgua (idUsuario, cantidad, registradaEn) VALUES (:idUsuario, :cantidad, :registradaEn)", nativeQuery = true)
    void registrarCantidadAgua(@Param("idUsuario") Integer idUsuario,
                               @Param("cantidad") Float cantidad,
                               @Param("registradaEn") String registradaEn);

    // Obtener historial de agua por fecha
    @Query(value = "SELECT * FROM RegistroAgua WHERE idUsuario = :idUsuario AND DATE(registradaEn) = :fecha", nativeQuery = true)
    List<RegistroAgua> obtenerHistorialPorFecha(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);

    // Obtener cantidad total consumida por día
    @Query(value = "SELECT SUM(cantidad) FROM RegistroAgua WHERE idUsuario = :idUsuario AND DATE(registradaEn) = :fecha", nativeQuery = true)
    Float obtenerTotalConsumidoPorDia(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);
}
