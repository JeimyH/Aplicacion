package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.AlimentoRecienteDTO;
import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Repository.AlimentoRepository;
import com.example.Proyecto.Repository.UsuarioRepository;
import com.example.Proyecto.Service.AlimentoRecienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Reciente")
public class AlimentoRecienteController {
    @Autowired
    public AlimentoRecienteService alimentoRecienteService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @PostMapping("/registrar/{idUsuario}/{idAlimento}")
    public ResponseEntity<?> registrarReciente(@PathVariable Long idUsuario, @PathVariable Long idAlimento) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        Optional<Alimento> alimentoOpt = alimentoRepository.findById(idAlimento);

        if (usuarioOpt.isPresent() && alimentoOpt.isPresent()) {
            alimentoRecienteService.registrarAlimentoReciente(idUsuario, alimentoOpt.get(), usuarioOpt.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Usuario o alimento no encontrado");
        }
    }

    @GetMapping("/consultar/{idUsuario}")
    public ResponseEntity<List<AlimentoRecienteDTO>> obtenerRecientes(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(alimentoRecienteService.obtenerRecientesPorUsuario(idUsuario));
    }

    @DeleteMapping("/eliminarTodos/{idUsuario}")
    public ResponseEntity<?> eliminarTodosRecientes(@PathVariable Long idUsuario) {
        alimentoRecienteService.eliminarTodosPorUsuario(idUsuario);
        return ResponseEntity.ok("Recientes eliminados");
    }

    @DeleteMapping("/eliminar/{idUsuario}/{idAlimento}")
    public ResponseEntity<?> eliminarReciente(
            @PathVariable Long idUsuario,
            @PathVariable Long idAlimento) {
        try {
            // System.out.println("Intentando eliminar alimento reciente: usuario=" + idUsuario + ", alimento=" + idAlimento);
            alimentoRecienteService.eliminarRecienteIndividual(idUsuario, idAlimento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // e.printStackTrace(); // <--- muestra el error real en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el alimento reciente");
        }
    }


}
