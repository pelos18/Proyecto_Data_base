package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @GetMapping
    public String listarReportes() {
        return "reportes/lista";
    }
}
