package com.example.Proyecto.Service;

import com.example.Proyecto.Model.TokenSesion;
import com.example.Proyecto.Repository.TokenSesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TokenSesionService {
    @Autowired
    public TokenSesionRepository tokenRepository;

    public List<TokenSesion> listarTokenSesion(){
        // Validacion para intentar obtener la lista de Tokens de Sesion
        try {
            List<TokenSesion> tokenSesions = tokenRepository.findAll();
            // Validar que la lista no sea nula
            if (tokenSesions == null) {
                throw new IllegalStateException("No se encontraron Tokens de Sesion.");
            }
            return tokenSesions;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar los Tokens de Sesion: " + e.getMessage(), e);
        }
    }

    public Optional<TokenSesion> listarPorIdTokenSesion(long id_token){
        try {
            Optional<TokenSesion> tokenSesion = tokenRepository.findById(id_token);
            if (tokenSesion.isPresent()) {
                return tokenSesion;
            } else {
                throw new IllegalStateException("No se encontraron Tokens de Sesion.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el Token de Sesion " + id_token +": "+ e.getMessage(), e);
        }
    }

    public TokenSesion guardarTokenSesion(TokenSesion tokenSesion){
        try{
            if(tokenSesion==null){
                throw new IllegalArgumentException("El Token de Sesion no puede ser nulo");

            }else{
                if (tokenSesion.getToken() == null || tokenSesion.getToken().isEmpty()) {
                    throw new IllegalArgumentException("El nombre del entrenador es obligatorio.");
                }else if(tokenSesion.getExpiracion() == null){
                    throw new IllegalArgumentException("La fecha de expiracion del Token de Sesion es obligatorio.");
                }
                return  tokenRepository.save(tokenSesion);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el Token de Sesion " + e.getMessage(), e);
        }
    }

    public void eliminarTokenSesion(long id_token){
        try {
            if (id_token<=0) {
                throw new IllegalArgumentException("El ID del Token de Sesion debe ser un número positivo.");
            }
            if (!tokenRepository.existsById(id_token)) {
                throw new NoSuchElementException("No se encontró un Token de Sesion con el ID: " + id_token);
            }
            tokenRepository.deleteById(id_token);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el Token de Sesion "+ id_token +": "+ e.getMessage(), e);
        }
    }

    public TokenSesion actualizarTokenSesion(long id_token, TokenSesion tokenActualizado){
        Optional<TokenSesion> tokenOpt = tokenRepository.findById(id_token);
        if(tokenOpt.isPresent()){
            TokenSesion tokenExistente = tokenOpt.get();
            tokenExistente.setToken(tokenActualizado.getToken());
            tokenExistente.setExpiracion(tokenActualizado.getExpiracion());
            tokenExistente.setRevocado(tokenActualizado.isRevocado());
            return tokenRepository.save(tokenExistente);
        }else{
            return null;
        }
    }

    public void obtenerToken(@Param("id_usuario") Integer id_usuario, @Param("token") String token){
        tokenRepository.generarToken(id_usuario,token);
    }

    public boolean obtenerTokenPorExpiracion(@Param("token") String token){
        return tokenRepository.validarTokenPorExpiracion(token);
    }

    public void obtenerTokenActivo(@Param("token") String token){
        tokenRepository.revocarTokenActivo(token);
    }
}
