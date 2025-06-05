package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.InteraccionChatbot;
import com.example.Proyecto.Service.InteraccionChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/InteraccionChatbot")
public class InteraccionChatbotController {
    @Autowired
    public InteraccionChatbotService interaccionService;

    @GetMapping("/listar")
    public ResponseEntity<List<InteraccionChatbot>> listarInteraccionesChatbot() {
        List<InteraccionChatbot> interaccionChatbots = interaccionService.listarInteraccionesChatbot();
        // Verificar si la lista está vacía
        if (interaccionChatbots.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(interaccionChatbots, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_interaccion}")
    public ResponseEntity<InteraccionChatbot> listarPorIdInteraccionChatbot(@PathVariable long id_interaccion){
        try {
            Optional<InteraccionChatbot> interaccionOpt = interaccionService.listarPorIdInteraccionChatbot(id_interaccion);
            return interaccionOpt.map(interaccionChatbot -> new ResponseEntity<>(interaccionChatbot, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<InteraccionChatbot> guardarInteraccionChatbot(@RequestBody InteraccionChatbot interaccionChatbot){
        try {
            InteraccionChatbot nuevoInteraccionChatbot = interaccionService.guardarInteraccionChatbot(interaccionChatbot);
            return new ResponseEntity<>(nuevoInteraccionChatbot, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_interaccion}")
    public ResponseEntity<Void> eliminarInteraccionChatbot(@PathVariable long id_interaccion){
        try {
            interaccionService.eliminarInteraccionChatbot(id_interaccion);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_interaccion}")
    public ResponseEntity<InteraccionChatbot> actualizarInteraccionChatbot(@PathVariable long id_interaccion, @RequestBody InteraccionChatbot interaccionActualizado){
        try {
            InteraccionChatbot interaccionChatbot = interaccionService.actualizarInteraccionChatbot(id_interaccion, interaccionActualizado);
            return new ResponseEntity<>(interaccionChatbot, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
