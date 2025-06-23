package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.service.ReporteInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/reportes")
public class ReporteInventarioController {

    @Autowired
    private ReporteInventarioService reporteInventarioService;

    @GetMapping("/inventario")
    public String mostrarReporteInventario(Model model) {
        try {
            Map<String, Object> reporte = reporteInventarioService.generarReporteCompleto();
            model.addAttribute("reporte", reporte);
            return "reportes/inventario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar reporte: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/inventario/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerReporteInventarioAPI() {
        try {
            Map<String, Object> reporte = reporteInventarioService.generarReporteCompleto();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
