package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        try {
            logger.info("=== CARGANDO DASHBOARD PARA USUARIO: {} ===", authentication.getName());
            
            // Verificar conexión a base de datos
            dashboardService.verificarConexionBaseDatos();
            
            // Obtener datos reales de la base de datos
            Long totalProductos = dashboardService.getTotalProductos();
            Long totalClientes = dashboardService.getTotalClientes();
            Long ventasHoy = dashboardService.getVentasHoy();
            java.math.BigDecimal ingresosHoy = dashboardService.getIngresosHoy();
            java.math.BigDecimal ingresosMes = dashboardService.getIngresosMes();

            // Agregar datos al modelo
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("totalClientes", totalClientes);
            model.addAttribute("ventasHoy", ventasHoy);
            model.addAttribute("ingresosHoy", ingresosHoy);
            model.addAttribute("ingresosMes", ingresosMes);
            model.addAttribute("productosStockBajo", dashboardService.getProductosStockBajo());
            model.addAttribute("ventasRecientes", dashboardService.getVentasRecientes());

            // Información del usuario
            model.addAttribute("usuario", authentication.getName());

            logger.info("=== DASHBOARD CARGADO CON DATOS REALES ===");
            logger.info("Productos: {}, Clientes: {}, Ventas Hoy: {}", totalProductos, totalClientes, ventasHoy);
            
            return "dashboard/index";

        } catch (Exception e) {
            logger.error("=== ERROR AL CARGAR DASHBOARD ===", e);
            model.addAttribute("error", "Error al conectar con la base de datos: " + e.getMessage());
            return "error";
        }
    }
}
