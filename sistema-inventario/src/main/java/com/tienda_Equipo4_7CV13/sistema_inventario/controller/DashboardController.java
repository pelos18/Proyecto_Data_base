package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.DashboardService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping
    public String dashboard(Model model) {
        try {
            // Obtener estadísticas para el dashboard
            model.addAttribute("totalProductos", dashboardService.getTotalProductos());
            model.addAttribute("totalVentas", dashboardService.getTotalVentasHoy());
            model.addAttribute("productosStockBajo", dashboardService.getProductosStockBajo());
            model.addAttribute("ventasRecientes", dashboardService.getVentasRecientes());
            model.addAttribute("ingresosDiarios", dashboardService.getIngresosDiarios());
            
            return "dashboard/index";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/ventas")
    public String ventasView(Model model) {
        model.addAttribute("titulo", "Gestión de Ventas");
        return "dashboard/ventas";
    }
    
    @GetMapping("/productos")
    public String productosView(Model model) {
        model.addAttribute("titulo", "Gestión de Productos");
        return "dashboard/productos";
    }
    
    @GetMapping("/inventario")
    public String inventarioView(Model model) {
        model.addAttribute("titulo", "Control de Inventario");
        return "dashboard/inventario";
    }
    
    @GetMapping("/clientes")
    public String clientesView(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        return "dashboard/clientes";
    }
    
    @GetMapping("/reportes")
    public String reportesView(Model model) {
        model.addAttribute("titulo", "Reportes y Estadísticas");
        return "dashboard/reportes";
    }
}
