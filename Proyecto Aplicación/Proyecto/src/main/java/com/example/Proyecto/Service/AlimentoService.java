package com.example.Proyecto.Service;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Model.UsuarioAlimentoFavorito;
import com.example.Proyecto.Model.UsuarioAlimentoKey;
import com.example.Proyecto.Repository.AlimentoRepository;
import com.example.Proyecto.Repository.UsuarioAlimentoFavoritoRepository;
import com.example.Proyecto.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlimentoService {
    @Autowired
    public AlimentoRepository alimentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioAlimentoFavoritoRepository favoritoRepository;

    public Optional<Alimento> listarPorIdAlimento(long idAlimento){
        try {
            Optional<Alimento> alimento = alimentoRepository.findById(idAlimento);
            if (alimento.isPresent()) {
                return alimento;
            } else {
                throw new IllegalStateException("No se encontraron alimentos.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el alimento " + idAlimento +": "+ e.getMessage(), e);
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

    public void eliminarAlimento(long idAlimento){
        try {
            if (idAlimento<=0) {
                throw new IllegalArgumentException("El ID del alimento debe ser un número positivo.");
            }
            if (!alimentoRepository.existsById(idAlimento)) {
                throw new NoSuchElementException("No se encontró un alimento con el ID: " + idAlimento);
            }
            alimentoRepository.deleteById(idAlimento);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el alimento "+ idAlimento +": "+ e.getMessage(), e);
        }
    }

    public Alimento actualizarAlimento(long idAlimento, Alimento alimentoActualizado){
        Optional<Alimento> alimentoOpt = alimentoRepository.findById(idAlimento);
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
            return alimentoRepository.save(alimentoExistente);
        }else{
            return null;
        }
    }

    public List<Alimento> obtenerAlimentosPorCategoria(String categoria){
        return alimentoRepository.filtrarAlimentosPorCategoria(categoria);
    }

    public List<Alimento> obtenerAlimentosPorUsuario(@Param("idUsuario") Long idUsuario){
        return alimentoRepository.consultarAlimentosPorUsuario(idUsuario);
    }

    public Alimento obtenerInfNutricional(Long idAlimento){
        return alimentoRepository.obtenerInformacionNutricional(idAlimento);
    }

    public Alimento obtenerAlimentoPorNombre(String nombre) {
        return alimentoRepository.BuscarPorNombreAlimento(nombre);
    }

    public List<Alimento> listarAlimentos() {
        return alimentoRepository.findAll();
    }

    public void agregarFavorito(Long idUsuario, Long idAlimento) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        Alimento alimento = alimentoRepository.findById(idAlimento).orElseThrow();

        UsuarioAlimentoKey key = new UsuarioAlimentoKey(idUsuario, idAlimento);

        if (!favoritoRepository.existsById(key)) {
            UsuarioAlimentoFavorito favorito = new UsuarioAlimentoFavorito();
            favorito.setId(key);
            favorito.setUsuario(usuario);
            favorito.setAlimento(alimento);
            favoritoRepository.save(favorito);
        }
    }

    public void eliminarFavorito(Long idUsuario, Long idAlimento) {
        favoritoRepository.deleteByUsuario_IdUsuarioAndAlimento_IdAlimento(idUsuario, idAlimento);
    }

    public List<Alimento> obtenerFavoritos(Long idUsuario) {
        List<UsuarioAlimentoFavorito> relaciones = favoritoRepository.findByUsuario_IdUsuario(idUsuario);
        return relaciones.stream()
                .map(UsuarioAlimentoFavorito::getAlimento)
                .collect(Collectors.toList());
    }

    public String obtenerUrlImagenPorNombre(String nombreAlimento) {
        return alimentoRepository.encontrarUrlImagenPorNombre(nombreAlimento);
    }
}
