package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @GetMapping
    public String listarProveedores() {
        return "proveedores/lista";
    }
}
