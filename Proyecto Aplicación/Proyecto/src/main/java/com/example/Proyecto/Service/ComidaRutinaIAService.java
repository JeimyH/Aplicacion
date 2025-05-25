package com.example.Proyecto.Service;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.ComidaRutinaIA;
import com.example.Proyecto.Repository.ComidaRutinaIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ComidaRutinaIAService {
    @Autowired
    public ComidaRutinaIARepository comidaRutinaIARepository;

    public List<ComidaRutinaIA> listarComidaRutinaIA(){
        // Validacion para intentar obtener la lista de la comida de la rutina de IA
        try {
            List<ComidaRutinaIA> comidaRutinaIAS = comidaRutinaIARepository.findAll();
            // Validar que la lista no sea nula
            if (comidaRutinaIAS == null) {
                throw new IllegalStateException("No se encontraron comidas en la rutina de IA.");
            }
            return comidaRutinaIAS;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar las comidas: " + e.getMessage(), e);
        }
    }

    public Optional<ComidaRutinaIA> listarPorIdComidaRutinaIA(long idComida){
        try {
            Optional<ComidaRutinaIA> comidaRutinaIA = comidaRutinaIARepository.findById(idComida);
            if (comidaRutinaIA.isPresent()) {
                return comidaRutinaIA;
            } else {
                throw new IllegalStateException("No se encontraron comidas.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar la comida " + idComida +": "+ e.getMessage(), e);
        }
    }

    public ComidaRutinaIA guardarComidaRutinaIA(ComidaRutinaIA comidaRutinaIA){
        try{
            if(comidaRutinaIA==null){
                throw new IllegalArgumentException("La comida no puede ser nula");
            }else{
                if (comidaRutinaIA.getTipoComida() == null) {
                    throw new IllegalArgumentException("El tipo de comida es obligatoria.");
                }else if(comidaRutinaIA.getDiaNumero() == 0){
                    throw new IllegalArgumentException("El dia de consumo de la comida de la rutina es obligatorio.");
                } else if (comidaRutinaIA.getDiaSemana() == null) {
                    throw new IllegalArgumentException("El dia de la semana de la rutina es obligatorio.");
                }
                return  comidaRutinaIARepository.save(comidaRutinaIA);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar la comida" + e.getMessage(), e);
        }
    }

    public void eliminarComidaRutinaIA(long idComida){
        try {
            if (idComida<=0) {
                throw new IllegalArgumentException("El ID de la comida debe ser un número positivo.");
            }
            if (!comidaRutinaIARepository.existsById(idComida)) {
                throw new NoSuchElementException("No se encontró una comida con el ID: " + idComida);
            }
            comidaRutinaIARepository.deleteById(idComida);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la comida "+ idComida +": "+ e.getMessage(), e);
        }
    }

    public ComidaRutinaIA actualizarComidaRutinaIA(long idComida, ComidaRutinaIA comidaRutinaIAActualizado){
        Optional<ComidaRutinaIA> comidaRutinaIAOpt = comidaRutinaIARepository.findById(idComida);
        if(comidaRutinaIAOpt.isPresent()){
            ComidaRutinaIA comidaRutinaIAExistente = comidaRutinaIAOpt.get();
            comidaRutinaIAExistente.setTipoComida(comidaRutinaIAActualizado.getTipoComida());
            comidaRutinaIAExistente.setAlimentosSugeridos(comidaRutinaIAActualizado.getAlimentosSugeridos());
            comidaRutinaIAExistente.setValoresNutricionales(comidaRutinaIAActualizado.getValoresNutricionales());
            comidaRutinaIAExistente.setTamanoPorciones(comidaRutinaIAActualizado.getTamanoPorciones());
            comidaRutinaIAExistente.setDiaNumero(comidaRutinaIAActualizado.getDiaNumero());
            comidaRutinaIAExistente.setDiaSemana(comidaRutinaIAActualizado.getDiaSemana());
            return comidaRutinaIARepository.save(comidaRutinaIAExistente);
        }else{
            return null;
        }
    }

    public List<Alimento>alimentosSugeridos(@Param("tipoComida") String tipoComida, @Param("fecha") String fecha) {
        return comidaRutinaIARepository.obtenerAlimentosSugeridos(tipoComida,fecha);
    }

    public void obtenerValoresNutricionales(@Param("idComida") Long idComida, @Param("valoresNutricionales") String valoresNutricionales){
        comidaRutinaIARepository.actualizarValoresNutricionales(idComida,valoresNutricionales);
    }

    public void obtenerAlimentoAComida(@Param("idComida") Long idComida, @Param("idAlimento") Long idAlimento, @Param("cantidad") Float cantidad){
        comidaRutinaIARepository.agregarAlimentoAComida(idComida,idAlimento,cantidad);
    }

    public void eliminarAlimentoDeComidas(@Param("idComida") Long idComida, @Param("idAlimento") Long idAlimento){
        comidaRutinaIARepository.eliminarAlimentoDeComida(idComida,idAlimento);
    }
}
