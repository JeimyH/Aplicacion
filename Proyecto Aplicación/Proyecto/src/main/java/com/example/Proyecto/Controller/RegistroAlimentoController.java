package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.RegistroAlimentoEntradaDTO;
import com.example.Proyecto.DTO.RegistroAlimentoSalidaDTO;
import com.example.Proyecto.DTO.RegistroPorFechaYMomentoDTO;
import com.example.Proyecto.Model.RegistroAlimento;
import com.example.Proyecto.Service.RegistroAlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    //
    @GetMapping("/usuario/{idUsuario}/fecha/{fecha}/momento/{momento}")
    public List<RegistroAlimentoSalidaDTO> obtenerPorUsuarioFechaYMomento(
            @PathVariable Long idUsuario,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PathVariable String momento) {

        List<RegistroAlimento> registros = registroAlimentoService.obtenerPorUsuarioFechaYMomento(idUsuario, fecha, momento);
        return registros.stream().map(RegistroAlimentoSalidaDTO::new).toList();
    }

}
