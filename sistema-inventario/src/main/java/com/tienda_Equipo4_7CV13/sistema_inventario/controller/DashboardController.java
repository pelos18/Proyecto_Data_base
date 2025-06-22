package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.dto.DashboardDTO;
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
            logger.info("Accediendo al dashboard - Usuario: {}", authentication.getName());

            // Obtener datos del dashboard
            DashboardDTO dashboardData = dashboardService.getDashboardData();
            model.addAttribute("dashboard", dashboardData);

            // Información del usuario
            model.addAttribute("usuario", authentication.getName());

            logger.info("Dashboard cargado exitosamente");
            return "dashboard/index";
        } catch (Exception e) {
            logger.error("Error al cargar dashboard: ", e);
            model.addAttribute("error", "Error al cargar los datos del dashboard: " + e.getMessage());
            return "error";
        }
    }
}
