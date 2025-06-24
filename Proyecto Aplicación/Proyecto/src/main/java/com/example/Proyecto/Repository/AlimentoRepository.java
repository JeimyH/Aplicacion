package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    // Filtrar alimentos por categoría
    @Query(value = "SELECT * FROM Alimento WHERE categoria = :categoria", nativeQuery = true)
    List<Alimento> filtrarAlimentosPorCategoria(@Param("categoria") String categoria);

    // Consultar alimentos creados por usuario
    @Query(value = "SELECT * FROM Alimento WHERE creador = :id_usuario", nativeQuery = true)
    List<Alimento> consultarAlimentosPorUsuario(@Param("id_usuario") Long idUsuario);

    // Obtener información nutricional de un alimento
    @Query(value = "SELECT * FROM Alimento WHERE id_alimento = :id_alimento", nativeQuery = true)
    Alimento obtenerInformacionNutricional(@Param("id_alimento") Long idAlimento);

    // Metodo para verificar si un alimento existe por su nombre usando una consulta nativa
    @Query(value = "SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Alimento a WHERE a.nombre_alimento = :nombreAlimento", nativeQuery = true)
    boolean existeAlimento(@Param("nombreAlimento") String nombreAlimento);

    @Query(value = "SELECT * FROM Alimento WHERE nombre_alimento = :nombre", nativeQuery = true)
    Alimento BuscarPorNombreAlimento(@Param("nombre") String nombre);

    @Query(value = "SELECT url_imagen FROM Alimento WHERE nombre_alimento = :nombre LIMIT 1", nativeQuery = true)
    String encontrarUrlImagenPorNombre(@Param("nombre") String nombre);
}
