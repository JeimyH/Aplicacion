package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.ActividadDiaDTO;
import com.example.Proyecto.Service.ActividadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Actividad")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping("/dias-con-actividad/{idUsuario}")
    public ResponseEntity<List<ActividadDiaDTO>> obtenerDiasConActividad(@PathVariable Long idUsuario) {
        List<ActividadDiaDTO> actividad = actividadService.obtenerDiasConActividad(idUsuario);
        return ResponseEntity.ok(actividad);
    }
}



