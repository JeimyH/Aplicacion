package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.ConfiguracionAplicacion;
import com.example.Proyecto.Service.ConfiguracionAplicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Configuracion")
public class ConfiguracionAplicacionController {
    @Autowired
    public ConfiguracionAplicacionService configuracionService;

    @GetMapping("/listar")
    public ResponseEntity<List<ConfiguracionAplicacion>> listarConfiguracionAplicacion() {
        List<ConfiguracionAplicacion> configuracionAplicacions = configuracionService.listarConfiguracionesAplicacion();
        // Verificar si la lista está vacía
        if (configuracionAplicacions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(configuracionAplicacions, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_configuracion}")
    public ResponseEntity<ConfiguracionAplicacion> listarPorIdConfiguracionAplicacion(@PathVariable long id_configuracion){
        try {
            Optional<ConfiguracionAplicacion> configuracionOpt = configuracionService.listarPorIdConfiguracionAplicacion(id_configuracion);
            return configuracionOpt.map(configuracionAplicacion -> new ResponseEntity<>(configuracionAplicacion, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<ConfiguracionAplicacion> guardarConfiguracionAplicacion(@RequestBody ConfiguracionAplicacion configuracionAplicacion){
        try {
            ConfiguracionAplicacion nuevoConfiguracion = configuracionService.guardarConfiguracionAplicacion(configuracionAplicacion);
            return new ResponseEntity<>(nuevoConfiguracion, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_configuracion}")
    public ResponseEntity<Void> eliminarConfiguracionAplicacion(@PathVariable long id_configuracion){
        try {
            configuracionService.eliminarConfiguracionAplicacion(id_configuracion);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_configuracion}")
    public ResponseEntity<ConfiguracionAplicacion> actualizarConfiguracionAplicacion(@PathVariable long id_configuracion, @RequestBody ConfiguracionAplicacion configuracionActualizado){
        try {
            ConfiguracionAplicacion configuracionAplicacion = configuracionService.actualizarConfiguracionAplicacion(id_configuracion, configuracionActualizado);
            return new ResponseEntity<>(configuracionAplicacion, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
