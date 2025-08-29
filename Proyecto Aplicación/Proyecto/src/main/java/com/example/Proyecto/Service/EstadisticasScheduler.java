package com.example.Proyecto.Service;

import com.example.Proyecto.Model.*;
import com.example.Proyecto.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class EstadisticasScheduler {
/*
    @Autowired
    public EstadisticasNutricionalesService estadisticasService;

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Scheduled(cron = "59 59 23 * * *") // Cada día a las 23:59:59
    public void procesarEstadisticasDiarias() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        LocalDate hoy = LocalDate.now();
        for (Usuario usuario : usuarios) {
            guardarEstadisticaDiaria(usuario.getIdUsuario(), hoy);
        }
    }

    @Scheduled(cron = "0 0 0 1 * *") // Cada primer día del mes a medianoche
    public void procesarEstadisticasMensuales() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        YearMonth mesAnterior = YearMonth.now().minusMonths(1);
        for (Usuario usuario : usuarios) {
            guardarEstadisticaMensual(usuario.getIdUsuario(), mesAnterior.getYear(), mesAnterior.getMonthValue());
        }

 */
}
