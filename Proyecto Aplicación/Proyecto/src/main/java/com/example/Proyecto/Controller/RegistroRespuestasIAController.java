package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.RegistroRespuestasIA;
import com.example.Proyecto.Service.RegistroRespuestasIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/RegistroRespuestasIA")
public class RegistroRespuestasIAController {
    @Autowired
    public RegistroRespuestasIAService respuestasIAService;

    @GetMapping("/listar")
    public ResponseEntity<List<RegistroRespuestasIA>> listarRegistroRespuestasIA() {
        List<RegistroRespuestasIA> registroRespuestasIAS = respuestasIAService.listarRegistrosRespuestasIA();
        // Verificar si la lista está vacía
        if (registroRespuestasIAS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(registroRespuestasIAS, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_respuestas}")
    public ResponseEntity<RegistroRespuestasIA> listarPorIdRegistroRespuestasIA(@PathVariable long id_respuestas){
        try {
            Optional<RegistroRespuestasIA> registroRespuestasIAOpt = respuestasIAService.listarPorIdRegistroRespuestasIA(id_respuestas);
            return registroRespuestasIAOpt.map(registroRespuestasIA -> new ResponseEntity<>(registroRespuestasIA, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<RegistroRespuestasIA> guardarRegistroRespuestasIA(@RequestBody RegistroRespuestasIA registroRespuestasIA){
        try {
            RegistroRespuestasIA nuevoRegistroRespuestasIA = respuestasIAService.guardarRegistroRespuestasIA(registroRespuestasIA);
            return new ResponseEntity<>(nuevoRegistroRespuestasIA, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_respuestas}")
    public ResponseEntity<Void> eliminarRegistroRespuestasIA(@PathVariable long id_respuestas){
        try {
            respuestasIAService.eliminarRegistroRespuestasIA(id_respuestas);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_respuestas}")
    public ResponseEntity<RegistroRespuestasIA> actualizarRegistroRespuestasIA(@PathVariable long id_respuestas, @RequestBody RegistroRespuestasIA registroRespuestasIAActualizado){
        try {
            RegistroRespuestasIA registroRespuestasIA = respuestasIAService.actualizarRegistroRespuestasIA(id_respuestas, registroRespuestasIAActualizado);
            return new ResponseEntity<>(registroRespuestasIA, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
