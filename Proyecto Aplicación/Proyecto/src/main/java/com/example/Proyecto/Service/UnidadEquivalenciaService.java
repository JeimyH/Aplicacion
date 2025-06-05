package com.example.Proyecto.Service;

import com.example.Proyecto.Model.UnidadEquivalencia;
import com.example.Proyecto.Repository.UnidadEquivalenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UnidadEquivalenciaService {
    @Autowired
    public UnidadEquivalenciaRepository unidadEquivalenciaRepository;

    public List<UnidadEquivalencia> listarUnidadEquivalencia(){
        // Validacion para intentar obtener la lista de las equivalencias de las unidades
        try {
            List<UnidadEquivalencia> unidadEquivalencias = unidadEquivalenciaRepository.findAll();
            // Validar que la lista no sea nula
            if (unidadEquivalencias == null) {
                throw new IllegalStateException("No se encontraron unidades de equivalencias.");
            }
            return unidadEquivalencias;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar los unidades de equivalencias: " + e.getMessage(), e);
        }
    }

    public Optional<UnidadEquivalencia> listarPorIdUnidadEquivalencia(long id_unidad){
        try {
            Optional<UnidadEquivalencia> unidadEquivalencia = unidadEquivalenciaRepository.findById(id_unidad);
            if (unidadEquivalencia.isPresent()) {
                return unidadEquivalencia;
            } else {
                throw new IllegalStateException("No se encontraron unidades de equivalencia.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar las unidades de equivalencia " + id_unidad +": "+ e.getMessage(), e);
        }
    }

    public UnidadEquivalencia guardarUnidadEquivalencia(UnidadEquivalencia unidadEquivalencia){
        try{
            if(unidadEquivalencia==null){
                throw new IllegalArgumentException("El Unidad de Equivalencia no puede ser nulo");

            }else{
                if (unidadEquivalencia.getUnidadOrigen() == null ||unidadEquivalencia.getUnidadOrigen().isEmpty()) {
                    throw new IllegalArgumentException("La unidad de origen del alimento es obligatorio.");
                }else if (unidadEquivalencia.getUnidadDestino() == null ||unidadEquivalencia.getUnidadDestino().isEmpty()) {
                    throw new IllegalArgumentException("La unidad de destino del alimento es obligatorio.");
                }else if (unidadEquivalencia.getFactorConversion() < 0  ) {
                    throw new IllegalArgumentException("El factor de conversion del alimento es obligatorio.");
                }
                return  unidadEquivalenciaRepository.save(unidadEquivalencia);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la unidad de equivalencia" + e.getMessage(), e);
        }
    }

    public void eliminarUnidadEquivalencia(long id_unidad){
        try {
            if (id_unidad<=0) {
                throw new IllegalArgumentException("El ID de la unidad de equivalencia debe ser un número positivo.");
            }
            if (!unidadEquivalenciaRepository.existsById(id_unidad)) {
                throw new NoSuchElementException("No se encontró un Registro de la unidad de equivalencia con el ID: " + id_unidad);
            }
            unidadEquivalenciaRepository.deleteById(id_unidad);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la unidad de equivalencia "+ id_unidad +": "+ e.getMessage(), e);
        }
    }

    public UnidadEquivalencia actualizarUnidadEquivalencia(long id_unidad, UnidadEquivalencia unidadEquivalenciaActualizado){
        Optional<UnidadEquivalencia> unidadEquivalenciaOpt = unidadEquivalenciaRepository.findById(id_unidad);
        if(unidadEquivalenciaOpt.isPresent()){
            UnidadEquivalencia unidadEquivalenciaExistente = unidadEquivalenciaOpt.get();
            unidadEquivalenciaExistente.setUnidadOrigen(unidadEquivalenciaActualizado.getUnidadOrigen());
            unidadEquivalenciaExistente.setUnidadDestino(unidadEquivalenciaActualizado.getUnidadDestino());
            unidadEquivalenciaExistente.setFactorConversion(unidadEquivalenciaActualizado.getFactorConversion());
            return unidadEquivalenciaRepository.save(unidadEquivalenciaExistente);
        }else{
            return null;
        }
    }
}
