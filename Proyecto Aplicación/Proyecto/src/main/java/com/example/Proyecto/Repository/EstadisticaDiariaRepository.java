package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.EstadisticaDiaria;
import com.example.Proyecto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadisticaDiariaRepository extends JpaRepository<EstadisticaDiaria, Long> {
    Optional<EstadisticaDiaria> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);

    @Query("SELECT e FROM EstadisticaDiaria e WHERE e.usuario = :usuario AND MONTH(e.fecha) = :mes AND YEAR(e.fecha) = :anio")
    List<EstadisticaDiaria> findByUsuarioAndMesAndAnio(@Param("usuario") Usuario usuario,
                                                       @Param("mes") int mes,
                                                       @Param("anio") int anio);
}