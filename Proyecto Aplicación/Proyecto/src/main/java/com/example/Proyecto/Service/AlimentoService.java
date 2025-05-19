package com.example.Proyecto.Service;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Repository.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AlimentoService {
    @Autowired
    public AlimentoRepository alimentoRepository;

    public List<Alimento> listarAlimentos(){
        // Validacion para intentar obtener la lista de alimentos
        try {
            List<Alimento> alimentos = alimentoRepository.findAll();
            // Validar que la lista no sea nula
            if (alimentos == null) {
                throw new IllegalStateException("No se encontraron alimentos.");
            }
            return alimentos;
        } catch (Exception e) {
            // Manejo de excepciones
            throw new RuntimeException("Error al listar a los alimentos: " + e.getMessage(), e);
        }
    }

    public Optional<Alimento> listarPorIdAlimento(long id_alimento){
        try {
            Optional<Alimento> alimento = alimentoRepository.findById(id_alimento);
            if (alimento.isPresent()) {
                return alimento;
            } else {
                throw new IllegalStateException("No se encontraron alimentos.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el alimento " + id_alimento +": "+ e.getMessage(), e);
        }
    }

    public Alimento guardarAlimento(Alimento alimento){
        try{
            if(alimento==null){
                throw new IllegalArgumentException("El alimento no puede ser nulo");
            }
            if (alimento.getNombreAlimento() == null || alimento.getNombreAlimento().isEmpty()) {
                throw new IllegalArgumentException("El nombre del alimento es obligatorio.");
            }else if(alimento.getCalorias() < 0 ){
                throw new IllegalArgumentException("Las caloriasa del alimento no pueden ser menor a 0.");
            }else if(alimento.getProteinas() < 0 ){
                throw new IllegalArgumentException("Las proteinas del alimento no pueden ser menor a 0.");
            }else if(alimento.getCarbohidratos() < 0 ){
                throw new IllegalArgumentException("Los carbohidratos del alimento no pueden ser menor a 0.");
            }else if(alimento.getGrasas() < 0 ){
                throw new IllegalArgumentException("Las grasas del alimento no pueden ser menor a 0.");
            }else if(alimento.getAzucares() < 0 ){
                throw new IllegalArgumentException("Los azucares del alimento no pueden ser menor a 0.");
            }else if(alimento.getFibra() < 0 ){
                throw new IllegalArgumentException("La fibra del alimento no pueden ser menor a 0.");
            }else if(alimento.getSodio() < 0 ){
                throw new IllegalArgumentException("El sodio del alimento no pueden ser menor a 0.");
            }else if(alimento.getGrasasSaturadas() < 0 ){
                throw new IllegalArgumentException("Las grasas saturadas del alimento no pueden ser menor a 0.");
            }else if(alimento.getCantidadBase() < 0 ){
                throw new IllegalArgumentException("La cantidad base del alimento no pueden ser menor a 0.");
            }else if(alimento.getUnidadBase() == null || alimento.getUnidadBase().isEmpty() ){
                throw new IllegalArgumentException("La unidad base del alimento no puede ser nula.");
            }
            // Validar que no se repita el alimento
            if (alimentoRepository.existeAlimento(alimento.getNombreAlimento())) {
                throw new IllegalArgumentException("El alimento ya existe en el sistema.");
            }
            return  alimentoRepository.save(alimento);
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el alimento" + e.getMessage(), e);
        }
    }

    public void eliminarAlimento(long id_alimento){
        try {
            if (id_alimento<=0) {
                throw new IllegalArgumentException("El ID del alimento debe ser un número positivo.");
            }
            if (!alimentoRepository.existsById(id_alimento)) {
                throw new NoSuchElementException("No se encontró un alimento con el ID: " + id_alimento);
            }
            alimentoRepository.deleteById(id_alimento);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el alimento "+ id_alimento +": "+ e.getMessage(), e);
        }
    }

    public Alimento actualizarAlimento(long id_alimento, Alimento alimentoActualizado){
        Optional<Alimento> alimentoOpt = alimentoRepository.findById(id_alimento);
        if(alimentoOpt.isPresent()){
            Alimento alimentoExistente = alimentoOpt.get();
            alimentoExistente.setNombreAlimento(alimentoActualizado.getNombreAlimento());
            alimentoExistente.setCalorias(alimentoActualizado.getCalorias());
            alimentoExistente.setProteinas(alimentoActualizado.getProteinas());
            alimentoExistente.setCarbohidratos(alimentoActualizado.getCarbohidratos());
            alimentoExistente.setGrasas(alimentoActualizado.getGrasas());
            alimentoExistente.setAzucares(alimentoActualizado.getAzucares());
            alimentoExistente.setFibra(alimentoActualizado.getFibra());
            alimentoExistente.setSodio(alimentoActualizado.getSodio());
            alimentoExistente.setGrasasSaturadas(alimentoActualizado.getGrasasSaturadas());
            alimentoExistente.setCategoria(alimentoActualizado.getCategoria());
            alimentoExistente.setUrlImagen(alimentoActualizado.getUrlImagen());
            alimentoExistente.setCantidadBase(alimentoActualizado.getCantidadBase());
            alimentoExistente.setUnidadBase(alimentoActualizado.getUnidadBase());
            // alimentoExistente.setFavorito(alimentoActualizado.isFavorito());
            return alimentoRepository.save(alimentoExistente);
        }else{
            return null;
        }
    }

    public List<Alimento> obtenerAlimentosPorCategoria(@Param("categoria") String categoria){
        return alimentoRepository.filtrarAlimentosPorCategoria(categoria);
    }

    public List<Alimento> obtenerAlimentosPorUsuario(@Param("id_usuario") Long id_usuario){
        return alimentoRepository.consultarAlimentosPorUsuario(id_usuario);
    }

    public Alimento obtenerInfNutricional(@Param("id_alimento") Long id_alimento){
        return alimentoRepository.obtenerInformacionNutricional(id_alimento);
    }

    public Alimento obtenerAlimentoPorNombre(String nombre) {
        return alimentoRepository.BuscarPorNombreAlimento(nombre);
    }
}
