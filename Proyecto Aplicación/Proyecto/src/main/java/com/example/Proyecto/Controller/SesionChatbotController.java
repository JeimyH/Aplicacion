package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.SesionChatbot;
import com.example.Proyecto.Service.SesionChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/SesionChatbot")
public class SesionChatbotController {
    @Autowired
    public SesionChatbotService sesionChatbotService;

    @GetMapping("/listar")
    public ResponseEntity<List<SesionChatbot>> listarSesionesChatbot() {
        List<SesionChatbot> sesionChatbots = sesionChatbotService.listarSesionesChatbot();
        // Verificar si la lista está vacía
        if (sesionChatbots.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(sesionChatbots, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_sesion}")
    public ResponseEntity<SesionChatbot> listarPorIdSesionChatbot(@PathVariable long id_sesion){
        try {
            Optional<SesionChatbot> sesionChatbotOpt = sesionChatbotService.listarPorIdSesionChatbot(id_sesion);
            return sesionChatbotOpt.map(sesionChatbot -> new ResponseEntity<>(sesionChatbot, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<SesionChatbot> guardarSesionChatbot(@RequestBody SesionChatbot sesionChatbot){
        try {
            SesionChatbot nuevoSesionChatbot = sesionChatbotService.guardarSesionChatbot(sesionChatbot);
            return new ResponseEntity<>(nuevoSesionChatbot, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_sesion}")
    public ResponseEntity<Void> eliminarSesionChatbot(@PathVariable long id_sesion){
        try {
            sesionChatbotService.eliminarSesionChatbot(id_sesion);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_sesion}")
    public ResponseEntity<SesionChatbot> actualizarSesionChatbot(@PathVariable long id_sesion, @RequestBody SesionChatbot sesionChatbotActualizado){
        try {
            SesionChatbot sesionChatbot = sesionChatbotService.actualizarSesionChatbot(id_sesion, sesionChatbotActualizado);
            return new ResponseEntity<>(sesionChatbot, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
