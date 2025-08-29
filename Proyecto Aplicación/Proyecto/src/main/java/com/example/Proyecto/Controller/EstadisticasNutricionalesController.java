package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.EstadisticaPorDiaDTO;
import com.example.Proyecto.DTO.EstadisticaPorMesDTO;
import com.example.Proyecto.DTO.NutrientesRecomendadosDTO;
import com.example.Proyecto.DTO.NutrientesTotalesDTO;
import com.example.Proyecto.Model.EstadisticasNutricionales;
import com.example.Proyecto.Service.EstadisticasNutricionalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/Estadisticas")
public class EstadisticasNutricionalesController {
    @Autowired
    public EstadisticasNutricionalesService estadisticasService;

    //Usando
    @GetMapping("/totales")
    public ResponseEntity<NutrientesTotalesDTO> obtenerTotales(
            @RequestParam Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        NutrientesTotalesDTO resultado = estadisticasService.obtenerTotalesPorFecha(idUsuario, fecha);
        return ResponseEntity.ok(resultado);
    }

    //Usando
    @GetMapping("/recomendados")
    public ResponseEntity<NutrientesRecomendadosDTO> obtenerRecomendaciones(@RequestParam Long idUsuario) {
        try {
            NutrientesRecomendadosDTO dto = estadisticasService.calcularRecomendacionesDiarias(idUsuario);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usuario no encontrado
        }
    }

    @GetMapping("/recomendados/mensual/{idUsuario}/{anio}/{mes}")
    public ResponseEntity<NutrientesRecomendadosDTO> getRecomendacionesMensuales(
            @PathVariable Long idUsuario,
            @PathVariable int anio,
            @PathVariable int mes) {

        NutrientesRecomendadosDTO dto = estadisticasService.calcularRecomendacionesMensuales(idUsuario, anio, mes);
        return ResponseEntity.ok(dto);
    }

    //Usando
    @GetMapping("/totalDiaria/{idUsuario}/mes")
    public ResponseEntity<List<EstadisticaPorDiaDTO>> getConsumoPorDia(
            @PathVariable Long idUsuario,
            @RequestParam int anio,
            @RequestParam int mes
    ) {
        YearMonth yearMonth = YearMonth.of(anio, mes);
        List<EstadisticaPorDiaDTO> datos = estadisticasService.obtenerConsumoPorDiaDelMes(idUsuario, yearMonth);
        return ResponseEntity.ok(datos);
    }

    //Usando
    @GetMapping("/totalMes/{idUsuario}/anio")
    public ResponseEntity<List<EstadisticaPorMesDTO>> getConsumoPorMes(
            @PathVariable Long idUsuario,
            @RequestParam int anio
    ) {
        List<EstadisticaPorMesDTO> datos = estadisticasService.obtenerConsumoPorMesDelAnio(idUsuario, anio);
        return ResponseEntity.ok(datos);
    }
    /**
     * Guardar o actualizar estadísticas diarias para un usuario en una fecha específica
     */
    @PostMapping("/diaria/{idUsuario}")
    public ResponseEntity<String> guardarEstadisticaDiaria(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        estadisticasService.guardarEstadisticaDiaria(idUsuario, fecha);
        return ResponseEntity.ok("✅ Estadística diaria guardada correctamente para el usuario " + idUsuario);
    }

    /**
     * Guardar o actualizar estadísticas mensuales para un usuario
     */
    @PostMapping("/mensual/{idUsuario}")
    public ResponseEntity<String> guardarEstadisticaMensual(
            @PathVariable Long idUsuario,
            @RequestParam int anio,
            @RequestParam int mes) {

        estadisticasService.guardarEstadisticaMensual(idUsuario, anio, mes);
        return ResponseEntity.ok(" Estadística mensual guardada correctamente para el usuario " + idUsuario);
    }

    /**
     * Forzar el procesamiento automático de todas las estadísticas diarias (como el cron)
     */
    @PostMapping("/procesar-diarias")
    public ResponseEntity<String> procesarDiarias() {
        estadisticasService.procesarEstadisticasDiarias();
        return ResponseEntity.ok("⏳ Procesamiento de estadísticas diarias ejecutado manualmente");
    }

    /**
     * Forzar el procesamiento automático de todas las estadísticas mensuales (como el cron)
     */
    @PostMapping("/procesar-mensuales")
    public ResponseEntity<String> procesarMensuales() {
        estadisticasService.procesarEstadisticasMensuales();
        return ResponseEntity.ok("⏳ Procesamiento de estadísticas mensuales ejecutado manualmente");
    }

    // Obtener estadísticas diarias calculadas desde la BD
    @GetMapping("/diaria")
    public ResponseEntity<EstadisticasNutricionales> obtenerEstadisticasDiarias(
            @RequestParam Long idUsuario,
            @RequestParam String fecha) {
        try {
            EstadisticasNutricionales est = estadisticasService.obtenerEstadisticasDiarias(idUsuario, fecha);
            if (est == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(est);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener progreso semanal
    @GetMapping("/progresoSemanal")
    public ResponseEntity<List<EstadisticasNutricionales>> obtenerProgresoSemanal(
            @RequestParam Long idUsuario,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            List<EstadisticasNutricionales> progresos =
                    estadisticasService.obtenerProgresosSemanales(idUsuario, fechaInicio, fechaFin);
            if (progresos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(progresos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener IMC
    @GetMapping("/imc")
    public ResponseEntity<Float> obtenerIMC(
            @RequestParam Long idUsuario,
            @RequestParam String fecha) {
        try {
            Float imc = estadisticasService.obtenerIMC(idUsuario, fecha);
            if (imc == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(imc);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Obtener total comidas registradas
    @GetMapping("/totalComidas")
    public ResponseEntity<Integer> obtenerTotalComidas(
            @RequestParam Long idUsuario,
            @RequestParam String fecha) {
        try {
            Integer total = estadisticasService.totalComidasRegistradas(idUsuario, fecha);
            if (total == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
