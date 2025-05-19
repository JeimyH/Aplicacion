package com.example.Proyecto.Service;

import com.example.Proyecto.Model.RegistroRespuestasIA;
import com.example.Proyecto.Repository.RegistroRespuestasIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegistroRespuestasIAService {
    @Autowired
    public RegistroRespuestasIARepository respuestasIARepository;

    public List<RegistroRespuestasIA> listarRegistrosRespuestasIA(){
        // Validacion para intentar obtener la lista de los Registros con las RespuestasIA
        try {
            List<RegistroRespuestasIA> registroRespuestasIAS = respuestasIARepository.findAll();
            // Validar que la lista no sea nula
            if (registroRespuestasIAS == null) {
                throw new IllegalStateException("No se encontraron registros con respuestas de IA.");
            }
            return registroRespuestasIAS;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar los registros con las respuestas de IA: " + e.getMessage(), e);
        }
    }

    public Optional<RegistroRespuestasIA> listarPorIdRegistroRespuestasIA(long id_respuesta){
        try {
            Optional<RegistroRespuestasIA> registroRespuestasIA = respuestasIARepository.findById(id_respuesta);
            if (registroRespuestasIA.isPresent()) {
                return registroRespuestasIA;
            } else {
                throw new IllegalStateException("No se encontraron registros de respuestas de IA.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el registro de la respuesta de IA " + id_respuesta +": "+ e.getMessage(), e);
        }
    }

    public RegistroRespuestasIA guardarRegistroRespuestasIA(RegistroRespuestasIA registroRespuestasIA){
        // Inicializa el campo creadoEn
        registroRespuestasIA.setCreadoEn(new Timestamp(System.currentTimeMillis()));
        try{
            if(registroRespuestasIA==null){
                throw new IllegalArgumentException("El registro de la respusta de IA no puede ser nulo");

            }else{
                if (registroRespuestasIA.getDatosRespuesta() == null || registroRespuestasIA.getDatosRespuesta().isEmpty()) {
                    throw new IllegalArgumentException("Los datos con la respuesta de IA son obligatorios.");
                }else if(registroRespuestasIA.getCreadoEn() == null){
                    throw new IllegalArgumentException("La fecha con la creacion de la respuesta de IA es obligatoria.");
                }
                return  respuestasIARepository.save(registroRespuestasIA);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el registro con la respuesta de IA" + e.getMessage(), e);
        }
    }

    public void eliminarRegistroRespuestasIA(long id_respuesta){
        try {
            if (id_respuesta<=0) {
                throw new IllegalArgumentException("El ID del registro de la respuesta de IA debe ser un número positivo.");
            }
            if (!respuestasIARepository.existsById(id_respuesta)) {
                throw new NoSuchElementException("No se encontró un registro con la respuesta de IA con el ID: " + id_respuesta);
            }
            respuestasIARepository.deleteById(id_respuesta);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el registro con la respuesta de IA "+ id_respuesta +": "+ e.getMessage(), e);
        }
    }

    public RegistroRespuestasIA actualizarRegistroRespuestasIA(long id_respuesta, RegistroRespuestasIA respuestaActualizado){
        Optional<RegistroRespuestasIA> respuestaOpt = respuestasIARepository.findById(id_respuesta);
        if(respuestaOpt.isPresent()){
            RegistroRespuestasIA respuestaExistente = respuestaOpt.get();
            respuestaExistente.setDatosRespuesta(respuestaActualizado.getDatosRespuesta());
            return respuestasIARepository.save(respuestaExistente);
        }else{
            return null;
        }
    }

    public void obtenerRespuestaGenerada(@Param("id_sesion") Integer id_sesion, @Param("datosRespuesta") String datosRespuesta){
        respuestasIARepository.guardarRespuestaGenerada(id_sesion,datosRespuesta);
    }

    public List<RegistroRespuestasIA> obtenerRespuestasAnteriores(@Param("id_sesion") Integer id_sesion){
        return respuestasIARepository.consultarRespuestasAnteriores(id_sesion);
    }
}
