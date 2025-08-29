package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.UnidadEquivalencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadEquivalenciaRepository extends JpaRepository<UnidadEquivalencia, Long> {

    Optional<UnidadEquivalencia> findByAlimentoAndUnidadOrigenAndUnidadDestino(
            Alimento alimento, String unidadOrigen, String unidadDestino
    );

    // Obtener directamente solo las unidades de origen por ID de alimento
    @Query("SELECT DISTINCT u.unidadOrigen FROM UnidadEquivalencia u WHERE u.alimento.id = :idAlimento")
    List<String> findUnidadOrigenByAlimentoId(@Param("idAlimento") Long idAlimento);

    // Obtener directamente solo las unidades de origen por nombre de alimento (ignora mayúsculas/minúsculas)
    @Query("SELECT DISTINCT u.unidadOrigen FROM UnidadEquivalencia u WHERE LOWER(u.alimento.nombreAlimento) = LOWER(:nombreAlimento)")
    List<String> findUnidadOrigenByAlimentoNombre(@Param("nombreAlimento") String nombreAlimento);

}
