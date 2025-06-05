package com.example.Proyecto.Repository;

import com.example.Proyecto.Model.UsuarioAlimentoFavorito;
import com.example.Proyecto.Model.UsuarioAlimentoKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioAlimentoFavoritoRepository extends JpaRepository<UsuarioAlimentoFavorito, UsuarioAlimentoKey> {

    List<UsuarioAlimentoFavorito> findByUsuario_IdUsuario(Long idUsuario);

    boolean existsByUsuario_IdUsuarioAndAlimento_IdAlimento(Long idUsuario, Long idAlimento);

    @Modifying
    @Transactional
    void deleteByUsuario_IdUsuarioAndAlimento_IdAlimento(Long idUsuario, Long idAlimento);
}

