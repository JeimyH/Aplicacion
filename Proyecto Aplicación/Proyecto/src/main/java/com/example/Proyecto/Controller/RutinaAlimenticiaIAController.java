package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.RutinaAlimenticiaIA;
import com.example.Proyecto.Service.RutinaAlimenticiaIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/RutinaIA")
public class RutinaAlimenticiaIAController {
    @Autowired
    public RutinaAlimenticiaIAService rutinaIAService;

    @GetMapping("/listar")
    public ResponseEntity<List<RutinaAlimenticiaIA>> listarRutinaAlimenticiaIA() {
        List<RutinaAlimenticiaIA> rutinaAlimenticiaIAS = rutinaIAService.listarRutinasAlimenticiaIA();
        // Verificar si la lista está vacía
        if (rutinaAlimenticiaIAS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(rutinaAlimenticiaIAS, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_rutina}")
    public ResponseEntity<RutinaAlimenticiaIA> listarPorIdRutinaAlimenticiaIA(@PathVariable long id_rutina){
        try {
            Optional<RutinaAlimenticiaIA> rutinaAlimenticiaIAOpt = rutinaIAService.listarPorIdRutinaAlimenticiaIA(id_rutina);
            return rutinaAlimenticiaIAOpt.map(rutinaAlimenticiaIA -> new ResponseEntity<>(rutinaAlimenticiaIA, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<RutinaAlimenticiaIA> guardarRutinaAlimenticiaIA(@RequestBody RutinaAlimenticiaIA rutinaAlimenticiaIA){
        try {
            RutinaAlimenticiaIA nuevoRutinaAlimenticiaIA = rutinaIAService.guardarRutinaAlimenticiaIA(rutinaAlimenticiaIA);
            return new ResponseEntity<>(nuevoRutinaAlimenticiaIA, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_rutina}")
    public ResponseEntity<Void> eliminarRutinaAlimenticiaIA(@PathVariable long id_rutina){
        try {
            rutinaIAService.eliminarRutinaAlimenticiaIA(id_rutina);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_rutina}")
    public ResponseEntity<RutinaAlimenticiaIA> actualizarRutinaAlimenticiaIA(@PathVariable long id_rutina, @RequestBody RutinaAlimenticiaIA rutinaAlimenticiaIAActualizado){
        try {
            RutinaAlimenticiaIA rutinaAlimenticiaIA = rutinaIAService.actualizarRutinaAlimenticiaIA(id_rutina, rutinaAlimenticiaIAActualizado);
            return new ResponseEntity<>(rutinaAlimenticiaIA, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
