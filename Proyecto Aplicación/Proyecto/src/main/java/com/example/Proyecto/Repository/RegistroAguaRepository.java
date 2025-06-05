package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RegistroAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RegistroAguaRepository extends JpaRepository<RegistroAgua, Long> {

    Optional<RegistroAgua> findByUsuario_IdUsuarioAndFecha(Long idUsuario, LocalDate fecha);

}
