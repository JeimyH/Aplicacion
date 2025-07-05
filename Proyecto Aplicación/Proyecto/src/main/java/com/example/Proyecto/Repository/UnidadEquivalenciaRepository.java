package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.UnidadEquivalencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadEquivalenciaRepository extends JpaRepository<UnidadEquivalencia, Long> {

    Optional<UnidadEquivalencia> findByAlimentoAndUnidadOrigenAndUnidadDestino(
            Alimento alimento, String unidadOrigen, String unidadDestino
    );

}
