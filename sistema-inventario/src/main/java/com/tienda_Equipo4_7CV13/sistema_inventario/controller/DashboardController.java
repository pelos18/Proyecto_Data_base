package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.DashboardService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        try {
            logger.info("=== ACCEDIENDO AL DASHBOARD ===");
            logger.info("Usuario: {}", authentication.getName());

            // Obtener datos individuales para el dashboard
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

            logger.info("Datos del dashboard:");
            logger.info("- Total productos: {}", totalProductos);
            logger.info("- Total clientes: {}", totalClientes);
            logger.info("- Ventas hoy: {}", ventasHoy);
            logger.info("- Ingresos hoy: {}", ingresosHoy);
            logger.info("- Ingresos mes: {}", ingresosMes);

            logger.info("=== DASHBOARD CARGADO EXITOSAMENTE ===");
            return "dashboard/index";

        } catch (Exception e) {
            logger.error("=== ERROR EN DASHBOARD ===", e);
            model.addAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            model.addAttribute("errorDetails", e.toString());
            return "error";
        }
    }

    @GetMapping("/ventas")
    public String ventasView(Model model) {
        try {
            logger.info("Accediendo a vista de ventas");
            model.addAttribute("titulo", "Gestión de Ventas");
            return "dashboard/ventas";
        } catch (Exception e) {
            logger.error("Error en vista de ventas: ", e);
            model.addAttribute("error", "Error al cargar la vista de ventas");
            return "error";
        }
    }

    @GetMapping("/productos")
    public String productosView(Model model) {
        try {
            logger.info("Accediendo a vista de productos");
            model.addAttribute("titulo", "Gestión de Productos");
            return "dashboard/productos";
        } catch (Exception e) {
            logger.error("Error en vista de productos: ", e);
            model.addAttribute("error", "Error al cargar la vista de productos");
            return "error";
        }
    }

    @GetMapping("/inventario")
    public String inventarioView(Model model) {
        try {
            logger.info("Accediendo a vista de inventario");
            model.addAttribute("titulo", "Control de Inventario");
            return "dashboard/inventario";
        } catch (Exception e) {
            logger.error("Error en vista de inventario: ", e);
            model.addAttribute("error", "Error al cargar la vista de inventario");
            return "error";
        }
    }

    @GetMapping("/clientes")
    public String clientesView(Model model) {
        try {
            logger.info("Accediendo a vista de clientes");
            model.addAttribute("titulo", "Gestión de Clientes");
            return "dashboard/clientes";
        } catch (Exception e) {
            logger.error("Error en vista de clientes: ", e);
            model.addAttribute("error", "Error al cargar la vista de clientes");
            return "error";
        }
    }

    @GetMapping("/reportes")
    public String reportesView(Model model) {
        try {
            logger.info("Accediendo a vista de reportes");
            model.addAttribute("titulo", "Reportes y Estadísticas");
            return "dashboard/reportes";
        } catch (Exception e) {
            logger.error("Error en vista de reportes: ", e);
            model.addAttribute("error", "Error al cargar la vista de reportes");
            return "error";
        }
    }
}
