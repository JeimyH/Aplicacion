package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.ComidaRutinaIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComidaRutinaIARepository extends JpaRepository<ComidaRutinaIA, Long> {
    // Obtener alimentos sugeridos por tipo de comida y fecha
    @Query(value = "SELECT A.* FROM ComidaRutinaIA CR " +
            "JOIN ComidaRutinaIA_Alimento CRA ON CR.idComida = CRA.idComida " +
            "JOIN Alimento A ON CRA.idAlimento = A.idAlimento " +
            "WHERE CR.tipoComida = :tipoComida AND CR.fecha = :fecha", nativeQuery = true)
    List<Alimento> obtenerAlimentosSugeridos(@Param("tipoComida") String tipoComida, @Param("fecha") String fecha);

    // Actualizar valores nutricionales de comida
    @Modifying
    @Query(value = "UPDATE ComidaRutinaIA SET valoresNutricionales = :valoresNutricionales WHERE idComida = :idComida", nativeQuery = true)
    void actualizarValoresNutricionales(@Param("idComida") Integer idComida, @Param("valoresNutricionales") String valoresNutricionales);

    // Agregar alimento a una comida en rutina
    @Modifying
    @Query(value = "INSERT INTO ComidaRutinaIA_Alimento (idComida, idAlimento, cantidad) VALUES (:idComida, :idAlimento, :cantidad)", nativeQuery = true)
    void agregarAlimentoAComida(@Param("idComida") Integer idComida, @Param("idAlimento") Integer idAlimento, @Param("cantidad") Float cantidad);

    // Eliminar alimento de una comida en rutina
    @Modifying
    @Query(value = "DELETE FROM ComidaRutinaIA_Alimento WHERE idComida = :idComida AND idAlimento = :idAlimento", nativeQuery = true)
    void eliminarAlimentoDeComida(@Param("idComida") Integer idComida, @Param("idAlimento") Integer idAlimento);



}
