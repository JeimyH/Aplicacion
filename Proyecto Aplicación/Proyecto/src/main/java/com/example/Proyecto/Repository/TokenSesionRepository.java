package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.TokenSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenSesionRepository extends JpaRepository<TokenSesion, Long> {
    // Generar token al iniciar sesión
    @Modifying
    @Query(value = "INSERT INTO TokenSesion (idUsuario, token, fechaCreacion, fechaExpiracion) VALUES (:idUsuario, :token, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 HOUR))", nativeQuery = true)
    void generarToken(@Param("idUsuario") Integer idUsuario, @Param("token") String token);

    // Validar token por expiración
    @Query(value = "SELECT CASE WHEN fechaExpiracion > CURRENT_TIMESTAMP THEN true ELSE false END FROM TokenSesion WHERE token = :token", nativeQuery = true)
    boolean validarTokenPorExpiracion(@Param("token") String token);

    // Revocar token activo
    @Modifying
    @Query(value = "DELETE FROM TokenSesion WHERE token = :token", nativeQuery = true)
    void revocarTokenActivo(@Param("token") String token);
}
