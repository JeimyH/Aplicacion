package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.UnidadEquivalenciaDTO;
import com.example.Proyecto.Model.UnidadEquivalencia;
import com.example.Proyecto.Service.UnidadEquivalenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/UnidadEquivalencia")
public class UnidadEquivalenciaController {
    @Autowired
    public UnidadEquivalenciaService unidadEquivalenciaService;

    @GetMapping("/listar")
    public ResponseEntity<List<UnidadEquivalencia>> listarUnidadEquivalencia() {
        List<UnidadEquivalencia> unidadEquivalencias = unidadEquivalenciaService.listarUnidadEquivalencia();
        // Verificar si la lista está vacía
        if (unidadEquivalencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(unidadEquivalencias, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_unidad}")
    public ResponseEntity<UnidadEquivalencia> listarPorIdUnidadEquivalencia(@PathVariable long id_unidad){
        try {
            Optional<UnidadEquivalencia> unidadEquivalenciaOpt = unidadEquivalenciaService.listarPorIdUnidadEquivalencia(id_unidad);
            return unidadEquivalenciaOpt.map(unidadEquivalencia -> new ResponseEntity<>(unidadEquivalencia, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<UnidadEquivalencia> guardarUnidadEquivalencia(@RequestBody UnidadEquivalencia unidadEquivalencia){
        try {
            UnidadEquivalencia nuevoUnidadEquivalencia = unidadEquivalenciaService.guardarUnidadEquivalencia(unidadEquivalencia);
            return new ResponseEntity<>(nuevoUnidadEquivalencia, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_unidad}")
    public ResponseEntity<Void> eliminarUnidadEquivalencia(@PathVariable long id_unidad){
        try {
            unidadEquivalenciaService.eliminarUnidadEquivalencia(id_unidad);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_unidad}")
    public ResponseEntity<UnidadEquivalencia> actualizarUnidadEquivalencia(@PathVariable long id_unidad, @RequestBody UnidadEquivalencia unidadEquivalenciaActualizado){
        try {
            UnidadEquivalencia unidadEquivalencia = unidadEquivalenciaService.actualizarUnidadEquivalencia(id_unidad, unidadEquivalenciaActualizado);
            return new ResponseEntity<>(unidadEquivalencia, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/actualizar")
    public ResponseEntity<UnidadEquivalencia> actualizarEquivalencia(@RequestBody UnidadEquivalenciaDTO dto) {
        UnidadEquivalencia actualizada = unidadEquivalenciaService.crearOActualizarEquivalencia(dto);
        return ResponseEntity.ok(actualizada);
    }
}
