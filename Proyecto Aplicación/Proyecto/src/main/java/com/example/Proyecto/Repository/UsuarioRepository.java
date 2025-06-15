package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.PreferenciasUsuario;
import com.example.Proyecto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca un usuario por su correo electrónico
    Optional<Usuario> findByCorreo(String correo);
    //Usuario findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    boolean existsByNombre(String nombre);


    // Obtener preferencias alimenticias
    @Query(value = "SELECT * FROM PreferenciasUsuario WHERE id_usuario = :id_usuario", nativeQuery = true)
    PreferenciasUsuario findPreferenciasByUsuarioId(@Param("id_usuario") Integer id_usuario);

    // Obtener preferencias y restricciones del usuario
    @Query(value = "SELECT * FROM PreferenciasUsuario WHERE idUsuario = :idUsuario", nativeQuery = true)
    PreferenciasUsuario findPreferenciasAndRestriccionesByUsuarioId(@Param("idUsuario") Integer idUsuario);
}
