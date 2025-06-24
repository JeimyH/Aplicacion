package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.RegistroAguaEntradaDTO;
import com.example.Proyecto.DTO.RegistroAguaRespuestaDTO;
import com.example.Proyecto.Model.RegistroAgua;
import com.example.Proyecto.Repository.UsuarioRepository;
import com.example.Proyecto.Service.RegistroAguaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/RegistroAgua")
@CrossOrigin(origins = "*")
public class RegistroAguaController {
    @Autowired
    public RegistroAguaService registroAguaService;

    @GetMapping("/listar")
    public ResponseEntity<List<RegistroAgua>> listarRegistroAgua() {
        List<RegistroAgua> registroAguas = registroAguaService.listarRegistrosAgua();
        // Verificar si la lista está vacía
        if (registroAguas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(registroAguas, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_registroAgua}")
    public ResponseEntity<RegistroAgua> listarPorIdRegistroAgua(@PathVariable long id_registroAgua){
        try {
            Optional<RegistroAgua> registroAguaOpt = registroAguaService.listarPorIdRegistroAgua(id_registroAgua);
            return registroAguaOpt.map(registroAgua -> new ResponseEntity<>(registroAgua, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<RegistroAgua> guardarRegistroAgua(@RequestBody RegistroAgua registroAgua){
        try {
            if (registroAgua.getUsuario() == null || registroAgua.getUsuario().getIdUsuario() == null) {
                throw new IllegalArgumentException("El ID del usuario es obligatorio.");
            }

            RegistroAgua nuevoRegistroAgua = registroAguaService.guardarRegistroAgua(registroAgua);
            return new ResponseEntity<>(nuevoRegistroAgua, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/eliminar/{id_registroAgua}")
    public ResponseEntity<Void> eliminarRegistroAgua(@PathVariable long id_registroAgua){
        try {
            registroAguaService.eliminarRegistroAgua(id_registroAgua);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_registroAgua}")
    public ResponseEntity<RegistroAgua> actualizarRegistroAgua(@PathVariable long id_registroAgua, @RequestBody RegistroAgua registroAguaActualizado){
        try {
            RegistroAgua registroAgua = registroAguaService.actualizarRegistroAgua(id_registroAgua, registroAguaActualizado);
            return new ResponseEntity<>(registroAgua, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/registrar/{idUsuario}")
    public ResponseEntity<RegistroAguaRespuestaDTO> registrarAgua(
            @PathVariable Long idUsuario,
            @RequestBody RegistroAguaEntradaDTO entrada) {

        RegistroAgua registro = registroAguaService.registrarAgua(idUsuario, entrada.getCantidadml());

        RegistroAguaRespuestaDTO dto = new RegistroAguaRespuestaDTO();
        dto.setIdRegistroAgua(registro.getIdRegistroAgua());
        dto.setIdUsuario(idUsuario);
        dto.setFecha(registro.getFecha().toString());
        dto.setCantidadml(registro.getCantidadml());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/obtener/{idUsuario}/hoy")
    public ResponseEntity<RegistroAguaRespuestaDTO> obtenerRegistroDeHoy(@PathVariable Long idUsuario) {
        RegistroAgua registro = registroAguaService.obtenerRegistroDeHoy(idUsuario);
        if (registro == null) {
            return ResponseEntity.ok(null);
        }

        RegistroAguaRespuestaDTO dto = new RegistroAguaRespuestaDTO();
        dto.setIdRegistroAgua(registro.getIdRegistroAgua());
        dto.setIdUsuario(idUsuario);
        dto.setFecha(registro.getFecha().toString());
        dto.setCantidadml(registro.getCantidadml());

        return ResponseEntity.ok(dto);
    }
}
