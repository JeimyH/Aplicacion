package com.example.Proyecto.Service;

import com.example.Proyecto.Model.PreferenciasUsuario;
import com.example.Proyecto.Repository.PreferenciasUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PreferenciasUsuarioService {
    @Autowired
    public PreferenciasUsuarioRepository preferenciaRepository;

    public List<PreferenciasUsuario> listarPreferenciasUsuario(){
        // Validacion para intentar obtener la lista de Preferencias de Usuario
        try {
            List<PreferenciasUsuario> preferenciasUsuarios = preferenciaRepository.findAll();
            // Validar que la lista no sea nula
            if (preferenciasUsuarios == null) {
                throw new IllegalStateException("No se encontraron Preferencias de Usuario.");
            }
            return preferenciasUsuarios;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar a las preferencias de Usuario: " + e.getMessage(), e);
        }
    }

    public Optional<PreferenciasUsuario> listarPorIdPreferenciasUsuario(long id_preferencia){
        try {
            Optional<PreferenciasUsuario> preferenciasUsuario = preferenciaRepository.findById(id_preferencia);
            if (preferenciasUsuario.isPresent()) {
                return preferenciasUsuario;
            } else {
                throw new IllegalStateException("No se encontraron Preferencias de Usuario.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la Preferencia de Usuario " + id_preferencia +": "+ e.getMessage(), e);
        }
    }

    public PreferenciasUsuario guardarPreferenciasUsuario(PreferenciasUsuario preferenciasUsuario){
        try{
            if(preferenciasUsuario==null) {
                throw new IllegalArgumentException("La Preferencia de Usuario no puede ser nulo");
            }else {
                return preferenciaRepository.save(preferenciasUsuario);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la preferencia de usuario" + e.getMessage(), e);
        }
    }

    public void eliminarPreferenciasUsuario(long id_preferencia){
        try {
            if (id_preferencia<=0) {
                throw new IllegalArgumentException("El ID de la Preferencia del Usuario debe ser un número positivo.");
            }
            if (!preferenciaRepository.existsById(id_preferencia)) {
                throw new NoSuchElementException("No se encontró una Preferencia del Usuario con el ID: " + id_preferencia);
            }
            preferenciaRepository.deleteById(id_preferencia);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la Preferencia de Usuario "+ id_preferencia +": "+ e.getMessage(), e);
        }
    }

    public PreferenciasUsuario actualizarPreferenciasUsuario(long id_preferencia, PreferenciasUsuario preferenciaActualizado){
        Optional<PreferenciasUsuario> preferenciaOpt = preferenciaRepository.findById(id_preferencia);
        if(preferenciaOpt.isPresent()){
            PreferenciasUsuario preferenciaExistente = preferenciaOpt.get();
            preferenciaExistente.setObjetivoAguaDiario(preferenciaActualizado.getObjetivoAguaDiario());
            preferenciaExistente.setComidasPreferidas(preferenciaActualizado.getComidasPreferidas());
            preferenciaExistente.setAlimentosExcluidos(preferenciaActualizado.getAlimentosExcluidos());
            preferenciaExistente.setConfiguracionesNotificaciones(preferenciaActualizado.getConfiguracionesNotificaciones());
            return preferenciaRepository.save(preferenciaExistente);
        }else{
            return null;
        }
    }

    public List<PreferenciasUsuario> obtenerPreferenciaPorUsuario(@Param("id_usuario") Integer id_usuario){
        return preferenciaRepository.obtenerPreferenciasPorUsuario(id_usuario);
    }

    public void obtenerPreferencias(@Param("id_usuario") Integer id_usuario,@Param("tipo") String tipo,@Param("valor") String valor,@Param("tipoAntiguo") String tipoAntiguo){
        preferenciaRepository.actualizarPreferencias(id_usuario,tipo,valor,tipo);
    }

    public void obtenerNuevaConfiguracion(@Param("id_usuario") Integer id_usuario,@Param("tipo") String tipo,@Param("valor") String valor){
        preferenciaRepository.insertarNuevaConfiguracion(id_usuario,tipo,valor);
    }
}
