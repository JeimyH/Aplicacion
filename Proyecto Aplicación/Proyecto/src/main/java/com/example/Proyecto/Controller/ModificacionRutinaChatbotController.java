package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.ModificacionRutinaChatbot;
import com.example.Proyecto.Service.ModificacionRutinaChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Modificacion")
public class ModificacionRutinaChatbotController {
    @Autowired
    public ModificacionRutinaChatbotService modRutChatbotService;

    @GetMapping("/listar")
    public ResponseEntity<List<ModificacionRutinaChatbot>> listarModificacionRutinaChatbot() {
        List<ModificacionRutinaChatbot> equipos = modRutChatbotService.listarModificacionesRutinaChatbot();
        // Verificar si la lista está vacía
        if (equipos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(equipos, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_modificacion}")
    public ResponseEntity<ModificacionRutinaChatbot> listarPorIdModificacionRutinaChatbot(@PathVariable long id_modificacion){
        try {
            Optional<ModificacionRutinaChatbot> modRutChatbotOpt = modRutChatbotService.listarPorIdModificacionRutinaChatbot(id_modificacion);
            return modRutChatbotOpt.map(modificacionRutinaChatbot -> new ResponseEntity<>(modificacionRutinaChatbot, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<ModificacionRutinaChatbot> guardarModificacionRutinaChatbot(@RequestBody ModificacionRutinaChatbot modificacionRutinaChatbot){
        try {
            ModificacionRutinaChatbot nuevoModRutChatbot = modRutChatbotService.guardarModificacionRutinaChatbot(modificacionRutinaChatbot);
            return new ResponseEntity<>(nuevoModRutChatbot, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_modificacion}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable long id_modificacion){
        try {
            modRutChatbotService.eliminarModificacionRutinaChatbot(id_modificacion);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_modificacion}")
    public ResponseEntity<ModificacionRutinaChatbot> actualizarModificacionRutinaChatbot(@PathVariable long id_modificacion, @RequestBody ModificacionRutinaChatbot modRutChatbotActualizado){
        try {
            ModificacionRutinaChatbot modificacionRutinaChatbot = modRutChatbotService.actualizarModificacionRutinaChatbot(id_modificacion, modRutChatbotActualizado);
            return new ResponseEntity<>(modificacionRutinaChatbot, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
