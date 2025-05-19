package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Service.RegistroAlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/RegistroAlimento")
public class RegistroAlimentoController {
    @Autowired
    public RegistroAlimentoService registroAlimentoService;

    @GetMapping("/listar")
    public ResponseEntity<List<RegistroAlimento>> listarRegistroAlimento() {
        List<RegistroAlimento> registroAlimentos = registroAlimentoService.listarRegistroAlimento();
        // Verificar si la lista está vacía
        if (registroAlimentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(registroAlimentos, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_registroAlimento}")
    public ResponseEntity<RegistroAlimento> listarPorIdRegistroAlimento(@PathVariable long id_registroAlimento){
        try {
            Optional<RegistroAlimento> registroAlimentoOpt = registroAlimentoService.listarPorIdRegistroAlimento(id_registroAlimento);
            return registroAlimentoOpt.map(registroAlimento -> new ResponseEntity<>(registroAlimento, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<RegistroAlimento> guardarRegistroAlimento(@RequestBody RegistroAlimento registroAlimento){
        try {
            RegistroAlimento nuevoRegistroAlimento = registroAlimentoService.guardarRegistroAlimento(registroAlimento);
            return new ResponseEntity<>(nuevoRegistroAlimento, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_registroAlimento}")
    public ResponseEntity<Void> eliminarRegistroAlimento(@PathVariable long id_registroAlimento){
        try {
            registroAlimentoService.eliminarRegistroAlimento(id_registroAlimento);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_registroAlimento}")
    public ResponseEntity<RegistroAlimento> actualizarRegistroAlimento(@PathVariable long id_registroAlimento, @RequestBody RegistroAlimento registroAlimentoActualizado){
        try {
            RegistroAlimento registroAlimento = registroAlimentoService.actualizarRegistroAlimento(id_registroAlimento, registroAlimentoActualizado);
            return new ResponseEntity<>(registroAlimento, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
