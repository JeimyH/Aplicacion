package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RegistroAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroAguaRepository extends JpaRepository<RegistroAgua, Long> {

    Optional<RegistroAgua> findByUsuario_IdUsuarioAndFecha(Long idUsuario, LocalDate fecha);

    @Modifying
    @Transactional
    @Query("DELETE FROM RegistroAgua r WHERE r.usuario.idUsuario = :idUsuario AND r.fecha = :fecha")
    void eliminarRegistroPorUsuarioYFecha(
            @Param("idUsuario") Long idUsuario,
            @Param("fecha") LocalDate fecha
    );

    @Query("SELECT DISTINCT r.fecha FROM RegistroAgua r WHERE r.usuario.id = :idUsuario")
    List<LocalDate> findFechasAguaPorUsuario(@Param("idUsuario") Long idUsuario);


}
