package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.UsuarioEntradaDTO;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public Optional<Usuario> listarPorIdUsuario(long id_usuario){
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id_usuario);
            if (usuario.isPresent()) {
                return usuario;
            } else {
                throw new IllegalStateException("No se encontraron usuarios.");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al listar el usuario " + id_usuario +": "+ e.getMessage(), e);
        }
    }

    public Usuario registrarUsuario(UsuarioEntradaDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (usuarioRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        Usuario nuevo = new Usuario();
        nuevo.setCorreo(dto.getCorreo());
        nuevo.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        nuevo.setNombre(dto.getNombre());
        nuevo.setFechaNacimiento(dto.getFechaNacimiento());
        nuevo.setAltura(dto.getAltura());
        nuevo.setPeso(dto.getPeso());
        nuevo.setSexo(dto.getSexo());
        nuevo.setPesoObjetivo(dto.getPesoObjetivo());
        nuevo.setObjetivosSalud(dto.getObjetivosSalud());
        nuevo.setRestriccionesDieta(dto.getRestriccionesDieta());
        nuevo.setNivelActividad(dto.getNivelActividad());
        nuevo.setCreadoEn(new Timestamp(System.currentTimeMillis()));

        return usuarioRepository.save(nuevo);
    }
    
    public Usuario autenticar(String correo, String contrasena) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                return usuario; // Login exitoso
            }
        }
        return null; // Usuario no encontrado o contraseña incorrecta
    }

    public void eliminarUsuario(long id_usuario){
        try {
            if (id_usuario<=0) {
                throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
            }
            if (!usuarioRepository.existsById(id_usuario)) {
                throw new NoSuchElementException("No se encontró un usuario con el ID: " + id_usuario);
            }
            usuarioRepository.deleteById(id_usuario);
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar el usuario "+ id_usuario +": "+ e.getMessage(), e);
        }
    }

    public Usuario actualizarUsuario(long id_usuario, Usuario usuarioActualizado){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id_usuario);
        if(usuarioOpt.isPresent()){
            Usuario usuarioExistente = usuarioOpt.get();
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
            usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
            usuarioExistente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
            usuarioExistente.setAltura(usuarioActualizado.getAltura());
            usuarioExistente.setPeso(usuarioActualizado.getPeso());
            usuarioExistente.setRestriccionesDieta(usuarioActualizado.getRestriccionesDieta());
            usuarioExistente.setObjetivosSalud(usuarioActualizado.getObjetivosSalud());
            usuarioExistente.setSexo(usuarioActualizado.getSexo());
            usuarioExistente.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(usuarioExistente);
        }else{
            return null;
        }
    }

    public Usuario actualizarAltura(long id_usuario, Float alturaActualizada){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id_usuario);
        if(usuarioOpt.isPresent()){
            Usuario usuarioExistente = usuarioOpt.get();
            usuarioExistente.setAltura(alturaActualizada);
            usuarioExistente.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(usuarioExistente);
        }else{
            return null;
        }
    }

    public Usuario actualizarPeso(long id_usuario, Float pesoActualizado){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id_usuario);
        if(usuarioOpt.isPresent()){
            Usuario usuarioExistente = usuarioOpt.get();
            usuarioExistente.setPeso(pesoActualizado);
            usuarioExistente.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(usuarioExistente);
        }else{
            return null;
        }
    }

    public Usuario actualizarPesoObjetivo(long id_usuario, Float pesoObjetivoActualizado){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id_usuario);
        if(usuarioOpt.isPresent()){
            Usuario usuarioExistente = usuarioOpt.get();
            usuarioExistente.setPesoObjetivo(pesoObjetivoActualizado);
            usuarioExistente.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(usuarioExistente);
        }else{
            return null;
        }
    }

    public Usuario actualizarDieta(long id_usuario, String dietaActualizada){
        return usuarioRepository.findById(id_usuario) .map(u -> {
            u.setRestriccionesDieta(dietaActualizada);
            u.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(u); }) .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

    }

    public Usuario actualizarObjetivo(long id_usuario, String objetivoActualizado){
        return usuarioRepository.findById(id_usuario) .map(u -> {
            u.setObjetivosSalud(objetivoActualizado);
            u.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(u); }) .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

    }

    public Usuario actualizarNivelActividad(long id_usuario, String nivelActividadActualizado){
        return usuarioRepository.findById(id_usuario) .map(u -> {
            u.setNivelActividad(nivelActividadActualizado);
            u.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(u); }) .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

    }

    public Usuario actualizarCorreo(long idUsuario, String correoActualizado) {
        return usuarioRepository.findById(idUsuario) .map(u -> {
            u.setCorreo(correoActualizado);
            u.setActualizadoEn(new Timestamp(System.currentTimeMillis()));
            return usuarioRepository.save(u); }) .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    }
}
