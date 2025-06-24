package com.example.Proyecto.Repository;

import com.example.Proyecto.DTO.RegistroPorFechaYMomentoDTO;
import com.example.Proyecto.Model.RegistroAlimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroAlimentoRepository extends JpaRepository<RegistroAlimento, Long> {

    List<RegistroAlimento> findByUsuario_IdUsuarioOrderByConsumidoEnDesc(Long idUsuario);

    @Query("SELECT r FROM RegistroAlimento r JOIN FETCH r.alimento WHERE r.usuario.idUsuario = :idUsuario ORDER BY r.consumidoEn DESC")
    List<RegistroAlimento> findRecientesConAlimento(@Param("idUsuario") Long idUsuario);

    @Query("SELECT r FROM RegistroAlimento r WHERE r.usuario.idUsuario = :idUsuario AND r.consumidoEn BETWEEN :inicio AND :fin AND r.momentoDelDia = :momento")
    List<RegistroAlimento> findByUsuarioFechaYMomento(
            @Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("momento") String momento
    );
}
