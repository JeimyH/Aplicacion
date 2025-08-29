package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RegistroAlimento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroAlimentoRepository extends JpaRepository<RegistroAlimento, Long> {

    @Query("SELECT r FROM RegistroAlimento r JOIN FETCH r.alimento WHERE r.usuario.idUsuario = :idUsuario ORDER BY r.consumidoEn DESC")
    List<RegistroAlimento> findRecientesConAlimento(@Param("idUsuario") Long idUsuario);

    @Query("SELECT r FROM RegistroAlimento r WHERE r.usuario.idUsuario = :idUsuario AND r.consumidoEn BETWEEN :inicio AND :fin AND r.momentoDelDia = :momento")
    List<RegistroAlimento> findByUsuarioFechaYMomento(
            @Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("momento") String momento
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM RegistroAlimento r WHERE r.usuario.idUsuario = :idUsuario AND r.momentoDelDia = :momento AND r.consumidoEn BETWEEN :inicio AND :fin")
    void deleteByUsuarioFechaYMomento(
            @Param("idUsuario") Long idUsuario,
            @Param("momento") String momento,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT DISTINCT DATE(r.consumidoEn) FROM RegistroAlimento r WHERE r.usuario.id = :idUsuario")
    List<java.sql.Date> findFechasComidaPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT r FROM RegistroAlimento r WHERE r.usuario.id = :idUsuario AND r.consumidoEn BETWEEN :inicio AND :fin")
    List<RegistroAlimento> findByUsuarioAndFecha(Long idUsuario, LocalDateTime inicio, LocalDateTime fin);


    @Query("SELECT r FROM RegistroAlimento r WHERE r.usuario.idUsuario = :idUsuario AND r.consumidoEn BETWEEN :inicio AND :fin AND r.momentoDelDia = :momento")
    List<RegistroAlimento> findByUsuarioAndFechaAndMomento(
            @Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("momento") String momento
    );

    @Query("SELECT COUNT(r) FROM RegistroAlimento r WHERE r.usuario.idUsuario = :idUsuario AND r.consumidoEn BETWEEN :inicio AND :fin")
    int countByUsuarioAndFecha(
            @Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
