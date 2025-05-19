package com.example.Proyecto.Controller;

import com.example.Proyecto.Model.TokenSesion;
import com.example.Proyecto.Service.TokenSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/TokenSesion")
public class TokenSesionController {
    @Autowired
    public TokenSesionService tokenService;

    @GetMapping("/listar")
    public ResponseEntity<List<TokenSesion>> listarTokenSesion() {
        List<TokenSesion> tokenSesions = tokenService.listarTokenSesion();
        // Verificar si la lista está vacía
        if (tokenSesions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(tokenSesions, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{id_token}")
    public ResponseEntity<TokenSesion> listarPorIdTokenSesion(@PathVariable long id_token){
        try {
            Optional<TokenSesion> tokenSesionOpt = tokenService.listarPorIdTokenSesion(id_token);
            return tokenSesionOpt.map(tokenSesion -> new ResponseEntity<>(tokenSesion, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<TokenSesion> guardarTokenSesion(@RequestBody TokenSesion tokenSesion){
        try {
            TokenSesion nuevoTokenSesion = tokenService.guardarTokenSesion(tokenSesion);
            return new ResponseEntity<>(nuevoTokenSesion, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{id_token}")
    public ResponseEntity<Void> eliminarTokenSesion(@PathVariable long id_token){
        try {
            tokenService.eliminarTokenSesion(id_token);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{id_token}")
    public ResponseEntity<TokenSesion> actualizarTokenSesion(@PathVariable long id_token, @RequestBody TokenSesion tokenSesionActualizado){
        try {
            TokenSesion tokenSesion = tokenService.actualizarTokenSesion(id_token, tokenSesionActualizado);
            return new ResponseEntity<>(tokenSesion, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}
