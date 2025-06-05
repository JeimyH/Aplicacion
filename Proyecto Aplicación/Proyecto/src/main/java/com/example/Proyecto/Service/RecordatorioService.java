package com.example.Proyecto.Service;

import com.example.Proyecto.Model.Recordatorio;
import com.example.Proyecto.Repository.RecordatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecordatorioService {
    @Autowired
    public RecordatorioRepository recordatorioRepository;

    public List<Recordatorio> listarRecordatorios(){
        // Validacion para intentar obtener la lista de Recordatorios
        try {
            List<Recordatorio> recordatorios = recordatorioRepository.findAll();
            // Validar que la lista no sea nula
            if (recordatorios == null) {
                throw new IllegalStateException("No se encontraron Recordatorios.");
            }
            return recordatorios;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar a los Recordatorio: " + e.getMessage(), e);
        }
    }

    public Optional<Recordatorio> listarPorIdRecordatorio(long id_recordatorio){
        try {
            Optional<Recordatorio> recordatorio = recordatorioRepository.findById(id_recordatorio);
            if (recordatorio.isPresent()) {
                return recordatorio;
            } else {
                throw new IllegalStateException("No se encontraron Recordatorios.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el Recordatorio " + id_recordatorio +": "+ e.getMessage(), e);
        }
    }

    public Recordatorio guardarRecordatorio(Recordatorio recordatorio){
        try{
            if(recordatorio==null){
                throw new IllegalArgumentException("El Recordatorio no puede ser nulo");
            }else{
                return  recordatorioRepository.save(recordatorio);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el recordatorio" + e.getMessage(), e);
        }
    }

    public void eliminarRecordatorio(long id_recordatorio){
        try {
            if (id_recordatorio<=0) {
                throw new IllegalArgumentException("El ID del Recordatorio debe ser un número positivo.");
            }
            if (!recordatorioRepository.existsById(id_recordatorio)) {
                throw new NoSuchElementException("No se encontró un Recordatorio con el ID: " + id_recordatorio);
            }
            recordatorioRepository.deleteById(id_recordatorio);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el Recordatorio "+ id_recordatorio +": "+ e.getMessage(), e);
        }
    }

    public Recordatorio actualizarRecordatorio(long id_recordatorio, Recordatorio recordatorioActualizado){
        Optional<Recordatorio> recordatorioOpt = recordatorioRepository.findById(id_recordatorio);
        if(recordatorioOpt.isPresent()){
            Recordatorio recordatorioExistente = recordatorioOpt.get();
            recordatorioExistente.setTipoRecordatorio(recordatorioActualizado.getTipoRecordatorio());
            recordatorioExistente.setMensaje(recordatorioActualizado.getMensaje());
            recordatorioExistente.setHora(recordatorioActualizado.getHora());
            recordatorioExistente.setActivo(recordatorioActualizado.isActivo());
            return recordatorioRepository.save(recordatorioExistente);
        }else{
            return null;
        }
    }

    public void obtenerNuevoRecordatorio(@Param("descripcion") String descripcion, @Param("hora") String hora, @Param("tipo") String tipo){
        recordatorioRepository.insertarNuevoRecordatorio(descripcion,hora,tipo);
    }

    public void obtenerDesactivarRecordatorio(@Param("id_recordatorio") Integer id_recordatorio, @Param("activo") boolean activo){
        recordatorioRepository.activarDesactivarRecordatorio(id_recordatorio,activo);
    }

    public List<Recordatorio> obtenerRecordatoriosPorHoraOTipo(@Param("hora") String hora, @Param("tipo") String tipo){
        return recordatorioRepository.buscarRecordatoriosPorHoraOTipo(hora,tipo);
    }
}
