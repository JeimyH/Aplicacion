package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.EstadisticaMensual;
import com.example.Proyecto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadisticaMensualRepository extends JpaRepository<EstadisticaMensual, Long> {
    Optional<EstadisticaMensual> findByUsuarioAndAnioAndMes(Usuario usuario, int anio, int mes);

    List<EstadisticaMensual> findByUsuarioIdUsuarioAndAnio(Long idUsuario, int anio);

}
