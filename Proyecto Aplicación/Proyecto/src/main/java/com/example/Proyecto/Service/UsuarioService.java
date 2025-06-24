package com.example.Proyecto.Service;

import com.example.Proyecto.DTO.UsuarioEntradaDTO;
import com.example.Proyecto.Model.PreferenciasUsuario;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

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

}
