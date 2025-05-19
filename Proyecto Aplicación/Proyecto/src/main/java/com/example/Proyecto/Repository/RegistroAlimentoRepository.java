package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RegistroAlimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RegistroAlimentoRepository extends JpaRepository<RegistroAlimento, Long> {
/*
    @Query(value = """
    SELECT * FROM registro_alimento 
    WHERE id_usuario = :idUsuario 
    AND consumido_en BETWEEN :fechaInicio AND :fechaFin
""", nativeQuery = true)
    List<RegistroAlimento> findRegistrosPorUsuarioYFecha(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") Timestamp fechaInicio,
            @Param("fechaFin") Timestamp fechaFin
    );

 */
    // Registrar alimento consumido
    @Modifying
    @Query(value = "INSERT INTO Registro_Alimento (id_usuario, id_alimento, tamanoPorcion, consumidoEn) VALUES (:idUsuario, :idAlimento, :tamanoPorcion, :consumidoEn)", nativeQuery = true)
    void registrarAlimentoConsumido(@Param("idUsuario") Integer idUsuario,
                                    @Param("idAlimento") Integer idAlimento,
                                    @Param("tamanoPorcion") Float tamanoPorcion,
                                    @Param("consumidoEn") String consumidoEn);

    // Obtener historial de alimentos por fecha
    @Query(value = "SELECT * FROM RegistroAlimento WHERE idUsuario = :idUsuario AND DATE(consumidoEn) = :fecha", nativeQuery = true)
    List<RegistroAlimento> obtenerHistorialPorFecha(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);

    // Calcular totales diarios por macronutriente
    @Query(value = "SELECT SUM(A.proteinas) AS totalProteinas, SUM(A.carbohidratos) AS totalCarbohidratos, SUM(A.grasas) AS totalGrasas " +
            "FROM RegistroAlimento RA " +
            "JOIN Alimento A ON RA.idAlimento = A.idAlimento " +
            "WHERE RA.idUsuario = :idUsuario AND DATE(RA.consumidoEn) = :fecha " +
            "GROUP BY DATE(RA.consumidoEn)", nativeQuery = true)
    List<Object[]> calcularTotalesDiarios(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha);

    // Eliminar registro de un alimento
    @Modifying
    @Query(value = "DELETE FROM RegistroAlimento WHERE idRegistro = :idRegistro", nativeQuery = true)
    void eliminarRegistroAlimento(@Param("idRegistro") Integer idRegistro);
}
