package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.ComidaRutinaIA;
import com.example.Proyecto.Service.ComidaRutinaIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/ComidaRutinaIA")
public class ComidaRutinaIAController {
    @Autowired
    public ComidaRutinaIAService comidaRutinaIAService;

    @GetMapping("/listar")
    public ResponseEntity<List<ComidaRutinaIA>> listarComidaRutinaIA() {
        List<ComidaRutinaIA> comidaRutinaIAS = comidaRutinaIAService.listarComidaRutinaIA();
        // Verificar si la lista está vacía
        if (comidaRutinaIAS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(comidaRutinaIAS, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_comida}")
    public ResponseEntity<ComidaRutinaIA> listarPorIdComidaRutinaIA(@PathVariable long id_comida){
        try {
            Optional<ComidaRutinaIA> comidaOpt = comidaRutinaIAService.listarPorIdComidaRutinaIA(id_comida);
            return comidaOpt.map(comidaRutinaIA -> new ResponseEntity<>(comidaRutinaIA, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<ComidaRutinaIA> guardarComidaRutinaIA(@RequestBody ComidaRutinaIA comidaRutinaIA){
        try {
            ComidaRutinaIA nuevoComida = comidaRutinaIAService.guardarComidaRutinaIA(comidaRutinaIA);
            return new ResponseEntity<>(nuevoComida, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_comida}")
    public ResponseEntity<Void> eliminarComidaRutinaIA(@PathVariable long id_comida){
        try {
            comidaRutinaIAService.eliminarComidaRutinaIA(id_comida);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_comida}")
    public ResponseEntity<ComidaRutinaIA> actualizarComidaRutinaIA(@PathVariable long id_comida, @RequestBody ComidaRutinaIA comidaActualizado){
        try {
            ComidaRutinaIA comidaRutinaIA = comidaRutinaIAService.actualizarComidaRutinaIA(id_comida, comidaActualizado);
            return new ResponseEntity<>(comidaRutinaIA, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
