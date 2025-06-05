package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.RegistroAlimentoEntradaDTO;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Service.RegistroAlimentoService;
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
    @Autowired
    private RegistroAlimentoService registroAlimentoService;

    @PostMapping("/registro")
    public ResponseEntity<RegistroAlimento> registrarAlimento(@RequestBody RegistroAlimentoEntradaDTO dto) {
        try {
            RegistroAlimento nuevo = registroAlimentoService.guardarRegistro(dto);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/recientes/{idUsuario}")
    public ResponseEntity<List<RegistroAlimento>> getRecientesPorUsuario(@PathVariable Long idUsuario) {
        List<RegistroAlimento> lista = registroAlimentoService.obtenerRecientesPorUsuario(idUsuario);
        return ResponseEntity.ok(lista);
    }

    /*
    @GetMapping("/agrupados/{idUsuario}")
    public ResponseEntity<Map<String, List<RegistroAlimento>>> obtenerRegistrosAgrupados(
            @PathVariable Long idUsuario,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        try {
            Map<String, List<RegistroAlimento>> agrupados = registroAlimentoService.obtenerRegistrosAgrupadosPorMomento(idUsuario, fecha);
            return ResponseEntity.ok(agrupados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

     */
}
