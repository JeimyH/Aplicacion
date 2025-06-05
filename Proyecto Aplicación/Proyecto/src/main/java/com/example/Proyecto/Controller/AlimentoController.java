package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.Alimento;
import com.example.Proyecto.Service.AlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/Alimento")
public class AlimentoController {
    @Autowired
    public AlimentoService alimentoService;

    /*
    @GetMapping("/listar")
    public ResponseEntity<List<Alimento>> listarAlimentos() {
        List<Alimento> alimentos = alimentoService.listarAlimentos();
        // Verificar si la lista está vacía
        if (alimentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(alimentos, HttpStatus.OK); // 200 OK
    }

     */

    @GetMapping("/buscar/{idAlimento}")
    public ResponseEntity<Alimento> listarPorIdAlimento(@PathVariable long idAlimento){
        try {
            Optional<Alimento> alimentoOpt = alimentoService.listarPorIdAlimento(idAlimento);
            return alimentoOpt.map(alimento -> new ResponseEntity<>(alimento, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Alimento> guardarAlimento(@RequestBody Alimento alimento){
        try {
            Alimento nuevoAlimento = alimentoService.guardarAlimento(alimento);
            return new ResponseEntity<>(nuevoAlimento, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_alimento}")
    public ResponseEntity<Void> eliminarAlimento(@PathVariable long id_alimento){
        try {
            alimentoService.eliminarAlimento(id_alimento);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_alimento}")
    public ResponseEntity<Alimento> actualizarAlimento(@PathVariable long id_alimento, @RequestBody Alimento alimentoActualizado){
        try {
            Alimento alimento = alimentoService.actualizarAlimento(id_alimento, alimentoActualizado);
            return new ResponseEntity<>(alimento, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @GetMapping("/alimentoCategoria/{categoria}")
    public ResponseEntity<List<Alimento>> obtenerAlimentosPorCategoria(@PathVariable String categoria) {
        List<Alimento> alimentos = alimentoService.obtenerAlimentosPorCategoria(categoria);
        if (alimentos != null && !alimentos.isEmpty()) {
            return ResponseEntity.ok(alimentos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/alimentosUsuario/{id_usuario}")
    public ResponseEntity<List<Alimento>> obtenerAlimentosPorUsuario(@Param("id_usuario") Long id_usuario){
        List<Alimento> alimentos = alimentoService.obtenerAlimentosPorUsuario(id_usuario);
        return new ResponseEntity<>(alimentos, HttpStatus.OK);
    }

    @GetMapping("/InfNutricional/{id_alimento}")
    public ResponseEntity<Alimento> obtenerInfNutricional(@Param("id_alimento") Long id_alimento){
        Alimento alimento = alimentoService.obtenerInfNutricional(id_alimento);
        return new ResponseEntity<>(alimento, HttpStatus.OK);
    }

    @GetMapping("/alimentoNombre/{nombre}")
    public ResponseEntity<Alimento> obtenerAlimento(@PathVariable String nombre) {
        Alimento alimento = alimentoService.obtenerAlimentoPorNombre(nombre);
        if (alimento != null) {
            return ResponseEntity.ok(alimento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Alimento>> listarTodos() {
        return ResponseEntity.ok(alimentoService.listarAlimentos());
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<Alimento> buscarPorNombre(@PathVariable String nombre) {
        Alimento alimento = alimentoService.obtenerAlimentoPorNombre(nombre);
        return alimento != null ? ResponseEntity.ok(alimento) : ResponseEntity.notFound().build();
    }

    @PostMapping("/favoritoAgregar/{idUsuario}/{idAlimento}")
    public ResponseEntity<?> agregarFavorito(@PathVariable Long idUsuario, @PathVariable Long idAlimento) {
        alimentoService.agregarFavorito(idUsuario, idAlimento);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favoritoEliminar/{idUsuario}/{idAlimento}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long idUsuario, @PathVariable Long idAlimento) {
        alimentoService.eliminarFavorito(idUsuario, idAlimento);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favoritos/{idUsuario}")
    public ResponseEntity<List<Alimento>> obtenerFavoritos(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(alimentoService.obtenerFavoritos(idUsuario));
    }
}
