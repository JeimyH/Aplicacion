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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    public UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        // Verificar si la lista está vacía
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_usuario}")
    public ResponseEntity<Usuario> listarPorIdUsuario(@PathVariable long id_usuario){
        try {
            Optional<Usuario> usuarioOpt = usuarioService.listarPorIdUsuario(id_usuario);
            return usuarioOpt.map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
        try {
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_usuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long id_usuario){
        try {
            usuarioService.eliminarUsuario(id_usuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_usuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable long id_usuario, @RequestBody Usuario usuarioActualizado){
        try {
            Usuario usuario = usuarioService.actualizarUsuario(id_usuario, usuarioActualizado);
            return new ResponseEntity<>(usuario, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    private UsuarioRespuestaDTO mapToResponse(Usuario usuario) {
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setId_usuario(usuario.getId_usuario());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setNombre(usuario.getNombre());
        respuesta.setFechaNacimiento(usuario.getFechaNacimiento());
        respuesta.setAltura(usuario.getAltura());
        respuesta.setPeso(usuario.getPeso());
        respuesta.setSexo(usuario.getSexo());
        respuesta.setObjetivosSalud(usuario.getObjetivosSalud());
        respuesta.setRestriccionesDieta(usuario.getRestriccionesDieta());
        return respuesta;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioRespuestaDTO> registrarUsuario(@RequestBody UsuarioEntradaDTO entradaDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(entradaDTO);
            UsuarioRespuestaDTO respuesta = mapToResponse(nuevoUsuario);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioRespuestaDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = usuarioService.autenticar(loginDTO.getCorreo(), loginDTO.getContrasena());
            UsuarioRespuestaDTO response = new UsuarioRespuestaDTO(usuario);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
