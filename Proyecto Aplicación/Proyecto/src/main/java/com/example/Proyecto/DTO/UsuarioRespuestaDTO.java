package com.example.Proyecto.DTO;

import com.example.Proyecto.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UsuarioRespuestaDTO {
    private Long idUsuario;
    private String correo;
    private String nombre;
    private LocalDate fechaNacimiento;
    private double altura;
    private double peso;
    private String sexo;
    private String restriccionesDieta;
    private String objetivosSalud;
    private Float pesoObjetivo;

    public UsuarioRespuestaDTO(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.correo = usuario.getCorreo();
        this.nombre = usuario.getNombre();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.altura = usuario.getAltura();
        this.peso = usuario.getPeso();
        this.sexo = usuario.getSexo();
        this.restriccionesDieta = usuario.getRestriccionesDieta();
        this.objetivosSalud = usuario.getObjetivosSalud();
        this.pesoObjetivo = usuario.getPesoObjetivo();
    }
}
