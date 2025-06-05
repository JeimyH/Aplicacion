package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.RegistroAlimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroAlimentoRepository extends JpaRepository<RegistroAlimento, Long> {

    List<RegistroAlimento> findByUsuario_IdUsuarioOrderByConsumidoEnDesc(Long idUsuario);
    //@Query("SELECT r FROM registro_alimento r WHERE r.usuario.id_usuario = :id_usuario AND DATE(r.consumido_en) = :fecha")
    //List<RegistroAlimento> findByUsuarioAndFecha(@Param("id_usuario") Long id_usuario, @Param("fecha") LocalDate fecha);

}
