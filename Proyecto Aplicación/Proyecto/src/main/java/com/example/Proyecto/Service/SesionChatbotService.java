package com.example.Proyecto.Service;

import com.example.Proyecto.Model.InteraccionChatbot;
import com.example.Proyecto.Model.SesionChatbot;
import com.example.Proyecto.Repository.SesionChatbotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SesionChatbotService {
    @Autowired
    public SesionChatbotRepository sesionRepository;

    public List<SesionChatbot> listarSesionesChatbot(){
        // Validacion para intentar obtener la lista de las sesiones del chatbot
        try {
            List<SesionChatbot> sesionChatbots = sesionRepository.findAll();
            // Validar que la lista no sea nula
            if (sesionChatbots == null) {
                throw new IllegalStateException("No se encontraron Sesiones del Chatbot.");
            }
            return sesionChatbots;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar las Sesiones del Chatbot: " + e.getMessage(), e);
        }
    }

    public Optional<SesionChatbot> listarPorIdSesionChatbot(long id_sesion){
        try {
            Optional<SesionChatbot> sesionChatbot = sesionRepository.findById(id_sesion);
            if (sesionChatbot.isPresent()) {
                return sesionChatbot;
            } else {
                throw new IllegalStateException("No se encontraron sesiones del chatbot.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la sesion del chatbot " + id_sesion +": "+ e.getMessage(), e);
        }
    }

    public SesionChatbot guardarSesionChatbot(SesionChatbot sesionChatbot){
        try{
            if(sesionChatbot==null){
                throw new IllegalArgumentException("La Sesion del Chatbot no puede ser nulo");

            }else{
                if (sesionChatbot.getInicioSesion() == null) {
                    throw new IllegalArgumentException("El inicio de sesion del chatbot es obligatorio.");
                }
                return  sesionRepository.save(sesionChatbot);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la Sesion Chatbot" + e.getMessage(), e);
        }
    }

    public void eliminarSesionChatbot(long id_sesion){
        try {
            if (id_sesion<=0) {
                throw new IllegalArgumentException("El ID de la Sesion del Chatbot debe ser un número positivo.");
            }
            if (!sesionRepository.existsById(id_sesion)) {
                throw new NoSuchElementException("No se encontró una Sesion de Chatbot con el ID: " + id_sesion);
            }
            sesionRepository.deleteById(id_sesion);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la Sesion del Chatbot "+ id_sesion +": "+ e.getMessage(), e);
        }
    }

    public SesionChatbot actualizarSesionChatbot(long id_sesion, SesionChatbot sesionActualizado){
        Optional<SesionChatbot> sesionOpt = sesionRepository.findById(id_sesion);
        if(sesionOpt.isPresent()){
            SesionChatbot sesionExistente = sesionOpt.get();
            sesionExistente.setInicioSesion(sesionActualizado.getInicioSesion());
            sesionExistente.setFinSesion(sesionActualizado.getFinSesion());
            sesionExistente.setMensajes(sesionActualizado.getMensajes());
            sesionExistente.setRetroalimentacion(sesionActualizado.getRetroalimentacion());
            return sesionRepository.save(sesionExistente);
        }else{
            return null;
        }
    }

    public List<SesionChatbot> sesionesActivas(@Param("id_usuario") Integer id_usuario){
        return sesionRepository.obtenerSesionesActivas(id_usuario);
    }

    public void obtenerNuevaSesion(@Param("id_usuario") Integer id_usuario){
        sesionRepository.crearNuevaSesion(id_usuario);
    }

    public void obtenerFinalizarSesion(@Param("id_sesion") Integer id_sesion){
        sesionRepository.finalizarSesion(id_sesion);
    }

    public List<InteraccionChatbot> mensajesYRecomendaciones(@Param("id_sesion") Integer id_sesion){
        return sesionRepository.obtenerMensajesYRecomendaciones(id_sesion);
    }
}
