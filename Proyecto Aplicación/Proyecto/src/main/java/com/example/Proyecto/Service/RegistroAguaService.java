package com.example.Proyecto.Service;

import com.example.Proyecto.Model.RegistroAgua;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.RegistroAguaRepository;
import com.example.Proyecto.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegistroAguaService {
    @Autowired
    public RegistroAguaRepository registroAguaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    public Optional<RegistroAgua> listarPorIdRegistroAgua(long idRegistroAgua){
        try {
            Optional<RegistroAgua> registroAgua = registroAguaRepository.findById(idRegistroAgua);
            if (registroAgua.isPresent()) {
                return registroAgua;
            } else {
                throw new IllegalStateException("No se encontraron Registros de Agua.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el Registro de Agua " + idRegistroAgua +": "+ e.getMessage(), e);
        }
    }

    public RegistroAgua guardarRegistroAgua(RegistroAgua registroAgua){
        // Inicializa el campo creadoEn
        //registroAgua.setRegistradoEn(new Timestamp(System.currentTimeMillis()));
        try{
            if(registroAgua==null){
                throw new IllegalArgumentException("El Registro de Agua no puede ser nulo");

            }else{
                if (registroAgua.getCantidadml() == 0) {
                    throw new IllegalArgumentException("La cantidad en ml del Registro de Agua es obligatorio.");
                }
                /*else if(registroAgua.getRegistradoEn() == null){
                    throw new IllegalArgumentException("El timestamp del registrado en del Registro de Agua es obligatorio.");
                }

                 */
                return  registroAguaRepository.save(registroAgua);
            }
        }catch (Exception e){
            throw new RuntimeException("Error al intentar guardar el Registro del Agua" + e.getMessage(), e);
        }
    }

    public void eliminarRegistroAgua(long idRegistroAgua){
        try {
            if (idRegistroAgua<=0) {
                throw new IllegalArgumentException("El ID del Registro de Agua debe ser un número positivo.");
            }
            if (!registroAguaRepository.existsById(idRegistroAgua)) {
                throw new NoSuchElementException("No se encontró un Registro de Agua con el ID: " + idRegistroAgua);
            }
            registroAguaRepository.deleteById(idRegistroAgua);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el Registro de Agua "+ idRegistroAgua +": "+ e.getMessage(), e);
        }
    }

    public RegistroAgua actualizarRegistroAgua(long idRegistroAgua, RegistroAgua registroAguaActualizado){
        Optional<RegistroAgua> registroAguaOpt = registroAguaRepository.findById(idRegistroAgua);
        if(registroAguaOpt.isPresent()){
            RegistroAgua registroAguaExistente = registroAguaOpt.get();
            registroAguaExistente.setCantidadml(registroAguaActualizado.getCantidadml());
            //registroAguaExistente.setRegistradoEn(new Timestamp(System.currentTimeMillis()));
            return registroAguaRepository.save(registroAguaExistente);
        }else{
            return null;
        }
    }

    public RegistroAgua registrarAgua(Long idUsuario, int cantidadml) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDate hoy = LocalDate.now();

        RegistroAgua registro = registroAguaRepository.findByUsuario_IdUsuarioAndFecha(idUsuario, hoy)
                .orElse(new RegistroAgua());

        registro.setUsuario(usuario);
        registro.setFecha(hoy);
        registro.setCantidadml(cantidadml);

        return registroAguaRepository.save(registro);
    }

    public RegistroAgua obtenerRegistroDeHoy(Long idUsuario) {
        return registroAguaRepository
                .findByUsuario_IdUsuarioAndFecha(idUsuario, LocalDate.now())
                .orElse(null);
    }

    // Nuevo metodo para obtener registro por fecha específica
    public RegistroAgua obtenerRegistroPorFecha(Long idUsuario, LocalDate fecha) {
        return registroAguaRepository
                .findByUsuario_IdUsuarioAndFecha(idUsuario, fecha)
                .orElse(null);
    }

}
