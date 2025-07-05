package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.NutrientesTotalesDTO;
import com.example.Proyecto.Model.EstadisticasNutricionales;
import com.example.Proyecto.Service.EstadisticasNutricionalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Estadisticas")
public class EstadisticasNutricionalesController {
    @Autowired
    public EstadisticasNutricionalesService estadisticasService;

    @GetMapping("/listar")
    public ResponseEntity<List<EstadisticasNutricionales>> listarEstadisticasNutricionales() {
        List<EstadisticasNutricionales> estadisticasNutricionales = estadisticasService.listarEstadisticasNutricionales();
        // Verificar si la lista está vacía
        if (estadisticasNutricionales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(estadisticasNutricionales, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_estadisticas}")
    public ResponseEntity<EstadisticasNutricionales> listarPorIdEstadisticasNutricionales(@PathVariable long id_estadisticas){
        try {
            Optional<EstadisticasNutricionales> estadisticasNutricionalesOpt = estadisticasService.listarPorIdEstadisticasNutricionales(id_estadisticas);
            return estadisticasNutricionalesOpt.map(estadisticasNutricionales -> new ResponseEntity<>(estadisticasNutricionales, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<EstadisticasNutricionales> guardarEstadisticasNutricionales(@RequestBody EstadisticasNutricionales estadisticasNutricionales){
        try {
            EstadisticasNutricionales nuevaEstadisticasNutricionales = estadisticasService.guardarEstadisticasNutricionales(estadisticasNutricionales);
            return new ResponseEntity<>(nuevaEstadisticasNutricionales, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_estadistica}")
    public ResponseEntity<Void> eliminarEstadisticasNutricionales(@PathVariable long id_estadistica){
        try {
            estadisticasService.eliminarEstadisticasNutricionales(id_estadistica);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_estadistica}")
    public ResponseEntity<EstadisticasNutricionales> actualizarEstadisticasNutricionales(@PathVariable long id_estadistica, @RequestBody EstadisticasNutricionales estadisticaActualizado){
        try {
            EstadisticasNutricionales estadisticasNutricionales = estadisticasService.actualizarEstadisticasNutricionales(id_estadistica, estadisticaActualizado);
            return new ResponseEntity<>(estadisticasNutricionales, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @GetMapping("/totales")
    public ResponseEntity<NutrientesTotalesDTO> obtenerTotales(
            @RequestParam Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        NutrientesTotalesDTO resultado = estadisticasService.obtenerTotalesPorFecha(idUsuario, fecha);
        return ResponseEntity.ok(resultado);
    }
}
