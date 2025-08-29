package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.RegistroAlimentoEntradaDTO;
import com.example.Proyecto.DTO.RegistroAlimentoSalidaDTO;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Service.RegistroAlimentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/RegistroAlimento")
public class RegistroAlimentoController {

    private static final Logger log = LoggerFactory.getLogger(RegistroAlimentoController.class);

    @Autowired
    private RegistroAlimentoService registroAlimentoService;

    /*
    @PostMapping("/registro")
    public ResponseEntity<RegistroAlimentoSalidaDTO> registrarAlimento(@RequestBody RegistroAlimentoEntradaDTO dto) {
        try {
            log.info("🟢 Recibido registro: ID Usuario={}, ID Alimento={}, Cantidad={}, Unidad={}, CantidadOriginal={}, UnidadOriginal={} Momento={}",
                    dto.getIdUsuario(), dto.getIdAlimento(), dto.getTamanoPorcion(),
                    dto.getUnidadMedida(), dto.getTamanoOriginal(), dto.getUnidadOriginal(), dto.getMomentoDelDia());

            RegistroAlimento nuevo = registroAlimentoService.guardarRegistro(dto);

            RegistroAlimentoSalidaDTO salida = new RegistroAlimentoSalidaDTO(nuevo);
            log.info("✅ Registro guardado: ID={}, {} {} - Original: {} {} - Fecha={}",
                    salida.getIdRegistroAlimento(), salida.getTamanoPorcion(), salida.getUnidadMedida(),
                    nuevo.getTamanoOriginal(), nuevo.getUnidadOriginal(), salida.getConsumidoEn());

            return ResponseEntity.ok(salida);
        } catch (Exception e) {
            log.error("❌ Error registrando alimento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

     */

    @PostMapping("/registro")
    public ResponseEntity<?> registrarAlimento(@RequestBody RegistroAlimentoEntradaDTO dto) {
        try {
            RegistroAlimento registro = registroAlimentoService.guardarRegistro(dto);
            return ResponseEntity.ok(registro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error al registrar el alimento"));
        }
    }

    @GetMapping("/recientes/{idUsuario}")
    public ResponseEntity<List<RegistroAlimento>> getRecientesPorUsuario(@PathVariable Long idUsuario) {
        List<RegistroAlimento> lista = registroAlimentoService.obtenerRecientesPorUsuario(idUsuario);
        return ResponseEntity.ok(lista);
    }

    // No lo estoy usando en el Front
    @GetMapping("/usuario/{idUsuario}/fecha/{fecha}/momento/{momento}")
    public List<RegistroAlimentoSalidaDTO> obtenerPorUsuarioFechaYMomento(
            @PathVariable Long idUsuario,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PathVariable String momento) {

        List<RegistroAlimento> registros = registroAlimentoService.obtenerPorUsuarioFechaYMomento(idUsuario, fecha, momento);
        return registros.stream().map(RegistroAlimentoSalidaDTO::new).toList();
    }

    @DeleteMapping("/eliminar/{idUsuario}/{momento}/{fecha}")
    public ResponseEntity<Void> eliminarRegistrosPorMomentoYFecha(
            @PathVariable Long idUsuario,
            @PathVariable String momento,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        registroAlimentoService.eliminarPorMomentoYFecha(idUsuario, momento, fecha);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/eliminar/{idRegistro}")
    public ResponseEntity<String> eliminarRegistroPorId(@PathVariable Long idRegistro) {
        registroAlimentoService.eliminarRegistroPorId(idRegistro);
        return ResponseEntity.ok("Registro eliminado con éxito");
    }

    //Obtener todas las unidades de origen de un alimento por su ID
    @GetMapping("/por-id/{idAlimento}")
    public ResponseEntity<List<String>> obtenerUnidadesPorIdAlimento(@PathVariable Long idAlimento) {
        try {
            List<String> unidades = registroAlimentoService.obtenerUnidadesPorIdAlimento(idAlimento);
            if (unidades.isEmpty()) {
                log.warn("No se encontraron unidades para Alimento ID {}", idAlimento);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(unidades);
        } catch (Exception e) {
            log.error("Error al obtener unidades por ID: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener todas las unidades de origen de un alimento por su nombre
    @GetMapping("/por-nombre")
    public ResponseEntity<List<String>> obtenerUnidadesPorNombreAlimento(@RequestParam String nombre) {
        try {
            List<String> unidades = registroAlimentoService.obtenerUnidadesPorNombreAlimento(nombre);
            if (unidades.isEmpty()) {
                log.warn("No se encontraron unidades para Alimento '{}'", nombre);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(unidades);
        } catch (Exception e) {
            log.error("Error al obtener unidades por nombre: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
