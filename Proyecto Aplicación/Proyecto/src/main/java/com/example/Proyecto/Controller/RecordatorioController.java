package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.Recordatorio;
import com.example.Proyecto.Service.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Recordatorio")
public class RecordatorioController {
    @Autowired
    public RecordatorioService recordatorioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Recordatorio>> listarRecordatorio() {
        List<Recordatorio> recordatorios = recordatorioService.listarRecordatorios();
        // Verificar si la lista está vacía
        if (recordatorios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(recordatorios, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_recordatorio}")
    public ResponseEntity<Recordatorio> listarPorIdRecordatorio(@PathVariable long id_recordatorio){
        try {
            Optional<Recordatorio> recordatorioOpt = recordatorioService.listarPorIdRecordatorio(id_recordatorio);
            return recordatorioOpt.map(recordatorio -> new ResponseEntity<>(recordatorio, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Recordatorio> guardarRecordatorio(@RequestBody Recordatorio recordatorio){
        try {
            Recordatorio nuevoRecordatorio = recordatorioService.guardarRecordatorio(recordatorio);
            return new ResponseEntity<>(nuevoRecordatorio, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_recordatorio}")
    public ResponseEntity<Void> eliminarRecordatorio(@PathVariable long id_recordatorio){
        try {
            recordatorioService.eliminarRecordatorio(id_recordatorio);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_recordatorio}")
    public ResponseEntity<Recordatorio> actualizarRecordatorio(@PathVariable long id_recordatorio, @RequestBody Recordatorio recordatorioActualizado){
        try {
            Recordatorio recordatorio = recordatorioService.actualizarRecordatorio(id_recordatorio, recordatorioActualizado);
            return new ResponseEntity<>(recordatorio, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
