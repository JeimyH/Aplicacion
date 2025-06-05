package com.example.Proyecto.Service;

import com.example.Proyecto.Model.RutinaAlimenticiaIA;
import com.example.Proyecto.Repository.RutinaAlimenticiaIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RutinaAlimenticiaIAService {
    @Autowired
    public RutinaAlimenticiaIARepository rutinaRepository;

    public List<RutinaAlimenticiaIA> listarRutinasAlimenticiaIA(){
        // Validacion para intentar obtener la lista de las Rutinas AlimenticiaIA
        try {
            List<RutinaAlimenticiaIA> rutinaAlimenticiaIAS = rutinaRepository.findAll();
            // Validar que la lista no sea nula
            if (rutinaAlimenticiaIAS == null) {
                throw new IllegalStateException("No se encontraron Rutinas AlimenticiaIA.");
            }
            return rutinaAlimenticiaIAS;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar las Rutinas AlimenticiaIA: " + e.getMessage(), e);
        }
    }

    public Optional<RutinaAlimenticiaIA> listarPorIdRutinaAlimenticiaIA(long id_rutina){
        try {
            Optional<RutinaAlimenticiaIA> rutinaAlimenticiaIA = rutinaRepository.findById(id_rutina);
            if (rutinaAlimenticiaIA.isPresent()) {
                return rutinaAlimenticiaIA;
            } else {
                throw new IllegalStateException("No se encontraron Rutinas AlimenticiaIA.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la Rutina AlimenticiaIA con el ID: " + id_rutina +": "+ e.getMessage(), e);
        }
    }

    public RutinaAlimenticiaIA guardarRutinaAlimenticiaIA(RutinaAlimenticiaIA rutinaAlimenticiaIA){
        // Inicializa el campo creadoEn
        rutinaAlimenticiaIA.setCreadoEn(new Timestamp(System.currentTimeMillis()));
        try{
            if(rutinaAlimenticiaIA==null){
                throw new IllegalArgumentException("La Rutina AlimenticiaIA no puede ser nula");

            }else{
                if (rutinaAlimenticiaIA.getFechaInicio() == null) {
                    throw new IllegalArgumentException("La fecha de inicio de la Rutina AlimenticiaIA es obligatorio.");
                }else if(rutinaAlimenticiaIA.getObjetivoCaloricoDia() == 0){
                    throw new IllegalArgumentException("El objetivo calorico diario de la Rutina AlimenticiaIA es obligatorio.");
                }else if(rutinaAlimenticiaIA.getCreadoEn() == null) {
                    throw new IllegalArgumentException("La fecha con la creacion de la Rutina AlimenticiaIA es obligatoria.");
                }
                return  rutinaRepository.save(rutinaAlimenticiaIA);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la Rutina AlimenticiaIA" + e.getMessage(), e);
        }
    }

    public void eliminarRutinaAlimenticiaIA(long id_rutina){
        try {
            if (id_rutina<=0) {
                throw new IllegalArgumentException("El ID de la Rutina AlimenticiaIA debe ser un número positivo.");
            }
            if (!rutinaRepository.existsById(id_rutina)) {
                throw new NoSuchElementException("No se encontró una Rutina AlimenticiaIA con el ID: " + id_rutina);
            }
            rutinaRepository.deleteById(id_rutina);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la Rutina AlimenticiaIA con el ID: "+ id_rutina +": "+ e.getMessage(), e);
        }
    }

    public RutinaAlimenticiaIA actualizarRutinaAlimenticiaIA(long id_rutina, RutinaAlimenticiaIA rutinaActualizado){
        Optional<RutinaAlimenticiaIA> rutinaOpt = rutinaRepository.findById(id_rutina);
        if(rutinaOpt.isPresent()){
            RutinaAlimenticiaIA rutinaExistente = rutinaOpt.get();
            rutinaExistente.setFechaInicio(rutinaActualizado.getFechaInicio());
            rutinaExistente.setFechaFin(rutinaActualizado.getFechaFin());
            rutinaExistente.setObjetivoCaloricoDia(rutinaActualizado.getObjetivoCaloricoDia());
            rutinaExistente.setDetalles(rutinaActualizado.getDetalles());
            rutinaExistente.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            rutinaExistente.setDias(rutinaActualizado.getDias());
            return rutinaRepository.save(rutinaExistente);
        }else{
            return null;
        }
    }

    public RutinaAlimenticiaIA RutinaPorUsuarioYFecha(@Param("idUsuario") Integer idUsuario, @Param("fecha") String fecha){
        return rutinaRepository.obtenerRutinaPorUsuarioYFecha(idUsuario,fecha);
    }

    public void obtenerNuevaRutina(@Param("id_usuario") Integer id_usuario,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin,@Param("objetivoCaloricoDiario") Float objetivoCaloricoDiario,@Param("detalles") String detalles){
        rutinaRepository.crearNuevaRutina(id_usuario,fechaInicio,fechaFin,objetivoCaloricoDiario,detalles);
    }

    public void obtenerRutina(@Param("id_rutina") Integer id_rutina,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin,@Param("objetivoCaloricoDiario") Float objetivoCaloricoDiario,@Param("detalles") String detalles){
        rutinaRepository.actualizarRutina(id_rutina,fechaInicio,fechaFin,objetivoCaloricoDiario,detalles);
    }

    public List<RutinaAlimenticiaIA> obtenerDetallesRutinaSemanal(@Param("id_usuario") Integer id_usuario,@Param("fechaInicio") String fechaInicio,@Param("fechaFin") String fechaFin){
        return rutinaRepository.consultarDetallesRutinaSemanal(id_usuario,fechaInicio,fechaFin);
    }

    public RutinaAlimenticiaIA RutinaPorDiaEspecifico(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return rutinaRepository.obtenerRutinaPorDiaEspecifico(id_usuario,fecha);
    }
}
