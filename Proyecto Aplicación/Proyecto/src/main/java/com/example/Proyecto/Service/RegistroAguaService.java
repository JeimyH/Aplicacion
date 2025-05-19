package com.example.Proyecto.Service;

import com.example.Proyecto.Model.RegistroAgua;
import com.example.Proyecto.Repository.RegistroAguaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegistroAguaService {
    @Autowired
    public RegistroAguaRepository registroAguaRepository;

    public List<RegistroAgua> listarRegistrosAgua(){
        // Validacion para intentar obtener la lista de Registros de Agua
        try {
            List<RegistroAgua> registroAguas = registroAguaRepository.findAll();
            // Validar que la lista no sea nula
            if (registroAguas == null) {
                throw new IllegalStateException("No se encontraron Registros de Agua.");
            }
            return registroAguas;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar los Registros de Agua: " + e.getMessage(), e);
        }
    }

    public Optional<RegistroAgua> listarPorIdRegistroAgua(long id_registroAgua){
        try {
            Optional<RegistroAgua> registroAgua = registroAguaRepository.findById(id_registroAgua);
            if (registroAgua.isPresent()) {
                return registroAgua;
            } else {
                throw new IllegalStateException("No se encontraron Registros de Agua.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el Registro de Agua " + id_registroAgua +": "+ e.getMessage(), e);
        }
    }

    public RegistroAgua guardarRegistroAgua(RegistroAgua registroAgua){
        // Inicializa el campo creadoEn
        registroAgua.setRegistradoEn(new Timestamp(System.currentTimeMillis()));
        try{
            if(registroAgua==null){
                throw new IllegalArgumentException("El Registro de Agua no puede ser nulo");

            }else{
                if (registroAgua.getCantidadml() == 0) {
                    throw new IllegalArgumentException("La cantidad en ml del Registro de Agua es obligatorio.");
                }else if(registroAgua.getRegistradoEn() == null){
                    throw new IllegalArgumentException("El timestamp del registrado en del Registro de Agua es obligatorio.");
                }
                return  registroAguaRepository.save(registroAgua);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el Registro del Agua" + e.getMessage(), e);
        }
    }

    public void eliminarRegistroAgua(long id_registroAgua){
        try {
            if (id_registroAgua<=0) {
                throw new IllegalArgumentException("El ID del Registro de Agua debe ser un número positivo.");
            }
            if (!registroAguaRepository.existsById(id_registroAgua)) {
                throw new NoSuchElementException("No se encontró un Registro de Agua con el ID: " + id_registroAgua);
            }
            registroAguaRepository.deleteById(id_registroAgua);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el Registro de Agua "+ id_registroAgua +": "+ e.getMessage(), e);
        }
    }

    public RegistroAgua actualizarRegistroAgua(long id_registroAgua, RegistroAgua registroAguaActualizado){
        Optional<RegistroAgua> registroAguaOpt = registroAguaRepository.findById(id_registroAgua);
        if(registroAguaOpt.isPresent()){
            RegistroAgua registroAguaExistente = registroAguaOpt.get();
            registroAguaExistente.setCantidadml(registroAguaActualizado.getCantidadml());
            registroAguaExistente.setRegistradoEn(new Timestamp(System.currentTimeMillis()));
            return registroAguaRepository.save(registroAguaExistente);
        }else{
            return null;
        }
    }

    public void obtenerCantidadAgua(@Param("id_usuario") Integer id_usuario, @Param("Cantidadml") Float cantidad, @Param("registradoEn") String registradaEn){
        registroAguaRepository.registrarCantidadAgua(id_usuario,cantidad,registradaEn);
    }

    public List<RegistroAgua> HistorialPorFecha(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return registroAguaRepository.obtenerHistorialPorFecha(id_usuario,fecha);
    }

    public Float TotalConsumidoPorDia(@Param("id_usuario") Integer id_usuario, @Param("fecha") String fecha){
        return registroAguaRepository.obtenerTotalConsumidoPorDia(id_usuario,fecha);
    }
}
