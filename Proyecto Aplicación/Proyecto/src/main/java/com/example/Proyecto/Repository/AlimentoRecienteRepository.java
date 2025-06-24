package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.AlimentoReciente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRecienteRepository extends JpaRepository<AlimentoReciente, Long> {
    // Consulta el historial de los alimentos recientes
    List<AlimentoReciente> findByUsuario_IdUsuarioOrderByConsultadoEnDesc(Long idUsuario);

    // Elimina uno específico
    @Modifying
    @Transactional
    void deleteByUsuario_IdUsuarioAndAlimento_IdAlimento(Long idUsuario, Long idAlimento);

    // Elimina todos los recientes de un usuario
    @Modifying
    @Transactional
    void deleteByUsuario_IdUsuario(Long idUsuario);

}
