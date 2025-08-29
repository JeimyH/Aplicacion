package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.LoginDTO;
import com.example.Proyecto.DTO.UsuarioEntradaDTO;
import com.example.Proyecto.DTO.UsuarioRespuestaDTO;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/Usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    public UsuarioService usuarioService;

    private UsuarioRespuestaDTO mapToResponse(Usuario usuario) {
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setIdUsuario(usuario.getIdUsuario());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setNombre(usuario.getNombre());
        respuesta.setFechaNacimiento(usuario.getFechaNacimiento());
        respuesta.setAltura(usuario.getAltura());
        respuesta.setPeso(usuario.getPeso());
        respuesta.setPesoObjetivo(usuario.getPesoObjetivo());
        respuesta.setSexo(usuario.getSexo());
        respuesta.setObjetivosSalud(usuario.getObjetivosSalud());
        respuesta.setRestriccionesDieta(usuario.getRestriccionesDieta());
        respuesta.setNivelActividad(usuario.getNivelActividad());
        return respuesta;
    }

    @GetMapping("/buscar/{idUsuario}")
    public ResponseEntity<Usuario> listarPorIdUsuario(@PathVariable long idUsuario){
        try {
            Optional<Usuario> usuarioOpt = usuarioService.listarPorIdUsuario(idUsuario);
            return usuarioOpt.map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> usuarioPorId(@PathVariable Long idUsuario) {
        try {
            Usuario usuario = usuarioService.usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            UsuarioRespuestaDTO respuesta = mapToResponse(usuario);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/existeCorreo")
    public ResponseEntity<Boolean> existeCorreo(@RequestParam String correo) {
        boolean existe = usuarioService.usuarioRepository.existsByCorreo(correo);
        return new ResponseEntity<>(existe, HttpStatus.OK);
    }

    @GetMapping("/existeNombre")
    public ResponseEntity<Boolean> existeNombre(@RequestParam String nombre) {
        boolean existe = usuarioService.usuarioRepository.existsByNombre(nombre);
        return new ResponseEntity<>(existe, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioEntradaDTO entradaDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(entradaDTO);
            UsuarioRespuestaDTO respuesta = mapToResponse(nuevoUsuario);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Devuelve un mensaje claro al frontend
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error inesperado al registrar usuario");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioRespuestaDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = usuarioService.autenticar(loginDTO.getCorreo(), loginDTO.getContrasena());

            if (usuario == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            UsuarioRespuestaDTO response = new UsuarioRespuestaDTO(usuario);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // <-- Esto es clave para ver el error real
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long idUsuario){
        try {
            usuarioService.eliminarUsuario(idUsuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable long idUsuario, @RequestBody Usuario usuarioActualizado){
        try {
            Usuario usuario = usuarioService.actualizarUsuario(idUsuario, usuarioActualizado);
            return new ResponseEntity<>(usuario, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizarAltura/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarAltura(@PathVariable long idUsuario, @RequestParam Float alturaActualizada){
        try {
            Usuario usuario = usuarioService.actualizarAltura(idUsuario, alturaActualizada);

            UsuarioRespuestaDTO usuarioDTO = new UsuarioRespuestaDTO(usuario);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizarPeso/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarPeso(@PathVariable long idUsuario, @RequestParam Float pesoActualizado){
        try {
            Usuario usuario = usuarioService.actualizarPeso(idUsuario, pesoActualizado);

            UsuarioRespuestaDTO usuarioDTO = new UsuarioRespuestaDTO(usuario);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizarPesoObjetivo/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarPesoObjetivo(@PathVariable long idUsuario, @RequestParam Float pesoObjetivoActualizado){
        try {
            Usuario usuario = usuarioService.actualizarPesoObjetivo(idUsuario, pesoObjetivoActualizado);

            UsuarioRespuestaDTO usuarioDTO = new UsuarioRespuestaDTO(usuario);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizarDieta/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarDieta(
            @PathVariable long idUsuario,
            @RequestParam String dietaActualizada){
        try {
            Usuario usuario = usuarioService.actualizarDieta(idUsuario, dietaActualizada);
            return ResponseEntity.ok(new UsuarioRespuestaDTO(usuario));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/actualizarObjetivo/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarObjetivo(
            @PathVariable long idUsuario,
            @RequestParam String objetivoActualizado){
        try {
            Usuario usuario = usuarioService.actualizarObjetivo(idUsuario, objetivoActualizado);
            return ResponseEntity.ok(new UsuarioRespuestaDTO(usuario));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/actualizarNivelAct/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarNivelActividad(
            @PathVariable long idUsuario,
            @RequestParam String nivelActividadActualizado){
        try {
            Usuario usuario = usuarioService.actualizarNivelActividad(idUsuario, nivelActividadActualizado);
            return ResponseEntity.ok(new UsuarioRespuestaDTO(usuario));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/actualizarCorreo/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarCorreo(
            @PathVariable long idUsuario,
            @RequestParam String correoActualizado) {
        try {
            Usuario usuario = usuarioService.actualizarCorreo(idUsuario, correoActualizado);
            return ResponseEntity.ok(new UsuarioRespuestaDTO(usuario));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
