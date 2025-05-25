package com.example.Proyecto.Service;

import com.example.Proyecto.Model.ConfiguracionAplicacion;
import com.example.Proyecto.Repository.ConfiguracionAplicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ConfiguracionAplicacionService {
    @Autowired
    public ConfiguracionAplicacionRepository configuracionAplicacionRepository;

    public List<ConfiguracionAplicacion> listarConfiguracionesAplicacion(){
        // Validacion para intentar obtener la lista de Configuraciones de la Aplicacion
        try {
            List<ConfiguracionAplicacion> configuracionAplicacions = configuracionAplicacionRepository.findAll();
            // Validar que la lista no sea nula
            if (configuracionAplicacions == null) {
                throw new IllegalStateException("No se encontraron Configuraciones de la Aplicacion.");
            }
            return configuracionAplicacions;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar las Configuraciones de la Aplicacion: " + e.getMessage(), e);
        }
    }

    public Optional<ConfiguracionAplicacion> listarPorIdConfiguracionAplicacion(long idConfiguracion){
        try {
            Optional<ConfiguracionAplicacion> configuracionAplicacion = configuracionAplicacionRepository.findById(idConfiguracion);
            if (configuracionAplicacion.isPresent()) {
                return configuracionAplicacion;
            } else {
                throw new IllegalStateException("No se encontraron configuraciones.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la Configuracion de la Aplicacion " + idConfiguracion +": "+ e.getMessage(), e);
        }
    }

    public ConfiguracionAplicacion guardarConfiguracionAplicacion(ConfiguracionAplicacion configuracionAplicacion){
        // Inicializa el campo creadoEn
        configuracionAplicacion.setCreadoEn(new Timestamp(System.currentTimeMillis()));
        try{
            if(configuracionAplicacion==null){
                throw new IllegalArgumentException("La configuracion de la aplicacion no puede ser nula");
            }else {
                return configuracionAplicacionRepository.save(configuracionAplicacion);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la configuracion" + e.getMessage(), e);
        }
    }

    public void eliminarConfiguracionAplicacion(long idConfiguracion){
        try {
            if (idConfiguracion<=0) {
                throw new IllegalArgumentException("El ID de la configuracion debe ser un número positivo.");
            }
            if (!configuracionAplicacionRepository.existsById(idConfiguracion)) {
                throw new NoSuchElementException("No se encontró una configuracion con el ID: " + idConfiguracion);
            }
            configuracionAplicacionRepository.deleteById(idConfiguracion);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la configuracion "+ idConfiguracion +": "+ e.getMessage(), e);
        }
    }

    public ConfiguracionAplicacion actualizarConfiguracionAplicacion(long idConfiguracion, ConfiguracionAplicacion configuracionAplicacionActualizado){
        Optional<ConfiguracionAplicacion> configuracionAplicacionOpt = configuracionAplicacionRepository.findById(idConfiguracion);
        if(configuracionAplicacionOpt.isPresent()){
            ConfiguracionAplicacion configuracionAplicacionExistente = configuracionAplicacionOpt.get();
            configuracionAplicacionExistente.setIdioma(configuracionAplicacionActualizado.getIdioma());
            configuracionAplicacionExistente.setNotificaciones(configuracionAplicacionActualizado.isNotificaciones());
            configuracionAplicacionExistente.setTema(configuracionAplicacionActualizado.getTema());
            configuracionAplicacionExistente.setFrecuenciaActualizacion(configuracionAplicacionActualizado.getFrecuenciaActualizacion());
            configuracionAplicacionExistente.setCreadoEn(configuracionAplicacionActualizado.getCreadoEn());
            return configuracionAplicacionRepository.save(configuracionAplicacionExistente);
        }else{
            return null;
        }
    }

    public List<ConfiguracionAplicacion> obtenerConfiguracionesDeUsuario(@Param("idUsuario") Long idUsuario){
        return configuracionAplicacionRepository.obtenerConfiguracionesDelUsuario(idUsuario);
    }

    public void actualizarIdiomaOUnidades(@Param("idUsuario") Long idUsuario, @Param("idioma") String idioma, @Param("unidades") String unidades){
        configuracionAplicacionRepository.actualizarIdiomaOUnidades(idUsuario,idioma,unidades);
    }

    public void activarDesactivarNotificacion(@Param("idUsuario") Long idUsuario, @Param("activadas") boolean activadas){
        configuracionAplicacionRepository.activarDesactivarNotificaciones(idUsuario,activadas);
    }
}
