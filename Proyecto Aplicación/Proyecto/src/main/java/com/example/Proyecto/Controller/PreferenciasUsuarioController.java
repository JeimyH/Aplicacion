package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.PreferenciasUsuario;
import com.example.Proyecto.Service.PreferenciasUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Preferencias")
public class PreferenciasUsuarioController {
    @Autowired
    public PreferenciasUsuarioService preferenciaService;

    @GetMapping("/listar")
    public ResponseEntity<List<PreferenciasUsuario>> listarPreferenciasUsuario() {
        List<PreferenciasUsuario> preferenciasUsuarios = preferenciaService.listarPreferenciasUsuario();
        // Verificar si la lista está vacía
        if (preferenciasUsuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(preferenciasUsuarios, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_preferencia}")
    public ResponseEntity<PreferenciasUsuario> listarPorIdPreferenciaUsuario(@PathVariable long id_preferencia){
        try {
            Optional<PreferenciasUsuario> preferenciaOpt = preferenciaService.listarPorIdPreferenciasUsuario(id_preferencia);
            return preferenciaOpt.map(preferenciasUsuario -> new ResponseEntity<>(preferenciasUsuario, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<PreferenciasUsuario> guardarPreferenciasUsuario(@RequestBody PreferenciasUsuario preferenciasUsuario){
        try {
            PreferenciasUsuario nuevoPreferencia = preferenciaService.guardarPreferenciasUsuario(preferenciasUsuario);
            return new ResponseEntity<>(nuevoPreferencia, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_preferencia}")
    public ResponseEntity<Void> eliminarPreferenciasUsuario(@PathVariable long id_preferencia){
        try {
            preferenciaService.eliminarPreferenciasUsuario(id_preferencia);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_preferencia}")
    public ResponseEntity<PreferenciasUsuario> actualizarPreferenciasUsuario(@PathVariable long id_preferencia, @RequestBody PreferenciasUsuario preferenciaActualizado){
        try {
            PreferenciasUsuario preferenciasUsuario = preferenciaService.actualizarPreferenciasUsuario(id_preferencia, preferenciaActualizado);
            return new ResponseEntity<>(preferenciasUsuario, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
