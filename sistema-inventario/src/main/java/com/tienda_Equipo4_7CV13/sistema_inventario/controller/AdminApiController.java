package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.service.ReporteInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private ReporteInventarioService reporteService;

    @GetMapping("/imprimir-inventario")
    public ResponseEntity<?> imprimirInventario() {
        try {
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("productos", reporteService.obtenerReporteCompleto());
            reporte.put("valorTotal", BigDecimal.ZERO); // Valor por defecto
            reporte.put("fecha", new Date());
            reporte.put("mensaje", "Reporte de inventario generado correctamente");

            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar reporte: " + e.getMessage());
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> getEstadisticas() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalProductos", reporteService.obtenerReporteCompleto().size());
            stats.put("fecha", new Date());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }
}
