package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        if (status != null) {
            model.addAttribute("status", status.toString());
        }
        
        if (error != null) {
            model.addAttribute("error", error.toString());
        } else {
            model.addAttribute("error", "Error interno del servidor");
        }
        
        if (exception != null) {
            model.addAttribute("message", exception.toString());
        } else {
            model.addAttribute("message", "Ha ocurrido un error inesperado");
        }
        
        if (requestUri != null) {
            model.addAttribute("path", requestUri.toString());
        }
        
        return "error";
    }
}
