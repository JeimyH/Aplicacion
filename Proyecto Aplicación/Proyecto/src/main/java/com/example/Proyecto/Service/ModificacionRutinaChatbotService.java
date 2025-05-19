package com.example.Proyecto.Service;

import com.example.Proyecto.Model.ModificacionRutinaChatbot;
import com.example.Proyecto.Repository.ModificacionRutinaChatbotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ModificacionRutinaChatbotService {
    @Autowired
    public ModificacionRutinaChatbotRepository modificacionRepository;

    public List<ModificacionRutinaChatbot> listarModificacionesRutinaChatbot(){
        // Validacion para intentar obtener la lista de las Modificaciones de la Rutina desde el Chatbot
        try {
            List<ModificacionRutinaChatbot> modificacionRutinaChatbots = modificacionRepository.findAll();
            // Validar que la lista no sea nula
            if (modificacionRutinaChatbots == null) {
                throw new IllegalStateException("No se encontraron equipos.");
            }
            return modificacionRutinaChatbots;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar las Modificaciones de la Rutina desde el Chatbot: " + e.getMessage(), e);
        }
    }

    public Optional<ModificacionRutinaChatbot> listarPorIdModificacionRutinaChatbot(long id_modificacion){
        try {
            Optional<ModificacionRutinaChatbot> modificacionRutinaChatbot = modificacionRepository.findById(id_modificacion);
            if (modificacionRutinaChatbot.isPresent()) {
                return modificacionRutinaChatbot;
            } else {
                throw new IllegalStateException("No se encontraron Modificaciones a la Rutina desde el Chatbot.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la Modificacion de la Rutina desde el Chatbot " + id_modificacion +": "+ e.getMessage(), e);
        }
    }

    public ModificacionRutinaChatbot guardarModificacionRutinaChatbot(ModificacionRutinaChatbot modRutChatbot){
        try{
            if(modRutChatbot==null){
                throw new IllegalArgumentException("La Modificacion de la Rutina desde el Chatbot no puede ser nula");

            }else{
                if (modRutChatbot.getFecha() == null) {
                    throw new IllegalArgumentException("La fecha de Modificacion de la Rutina desde el Chatbot es obligatoria.");
                }else if(modRutChatbot.getAccion() == null){
                    throw new IllegalArgumentException("La accion con la que se realizo la Modificacion de la Rutina desde el Chatbot es obligatoria.");
                }else if(modRutChatbot.getComida() ==null || modRutChatbot.getComida().isEmpty()) {
                    throw new IllegalArgumentException("La comida Modificada en la Rutina desde el Chatbot es obligatoria.");
                }
                return  modificacionRepository.save(modRutChatbot);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la Modificacion de la Rutina desde el Chatbot" + e.getMessage(), e);
        }
    }

    public void eliminarModificacionRutinaChatbot(long id_modificacion){
        try {
            if (id_modificacion<=0) {
                throw new IllegalArgumentException("El ID de la Modificacion de la Rutina desde el Chatbot debe ser un número positivo.");
            }
            if (!modificacionRepository.existsById(id_modificacion)) {
                throw new NoSuchElementException("No se encontró una Modificacion de la Rutina desde el Chatbot con el ID: " + id_modificacion);
            }
            modificacionRepository.deleteById(id_modificacion);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la Modificacion de la Rutina desde el Chatbot "+ id_modificacion +": "+ e.getMessage(), e);
        }
    }

    public ModificacionRutinaChatbot actualizarModificacionRutinaChatbot(long id_modificacion, ModificacionRutinaChatbot modRutChatbotActualizado){
        Optional<ModificacionRutinaChatbot> modRutChatbotOpt = modificacionRepository.findById(id_modificacion);
        if(modRutChatbotOpt.isPresent()){
            ModificacionRutinaChatbot modRutChatbotExistente = modRutChatbotOpt.get();
            modRutChatbotExistente.setFecha(modRutChatbotActualizado.getFecha());
            modRutChatbotExistente.setAccion(modRutChatbotActualizado.getAccion());
            modRutChatbotExistente.setComida(modRutChatbotActualizado.getComida());
            modRutChatbotExistente.setMotivo(modRutChatbotActualizado.getMotivo());
            return modificacionRepository.save(modRutChatbotExistente);
        }else{
            return null;
        }
    }

    public void obtenerModificacion(@Param("id_sesion") Integer id_sesion, @Param("tipoModificacion") String tipoModificacion, @Param("descripcion") String descripcion){
        modificacionRepository.registrarModificacion(id_sesion,tipoModificacion,descripcion);
    }

    public List<ModificacionRutinaChatbot> obtenerHistorialPorFechaa(@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin){
        return modificacionRepository.obtenerHistorialPorFecha(fechaInicio,fechaFin);
    }

    public List<ModificacionRutinaChatbot> obtenerPorTipoModificacion(@Param("tipoModificacion") String tipoModificacion){
        return modificacionRepository.filtrarPorTipoModificacion(tipoModificacion);
    }
}
