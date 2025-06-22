package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public String listarVentas(Model model) {
        try {
            List<Venta> ventas = ventaService.findAll();
            model.addAttribute("ventas", ventas);
            return "ventas/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar ventas: " + e.getMessage());
            return "ventas/lista";
        }
    }

    @GetMapping("/nueva")
    public String nuevaVentaForm(Model model) {
        model.addAttribute("venta", new Venta());
        return "ventas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta, 
                             RedirectAttributes redirectAttributes) {
        try {
            ventaService.save(venta);
            redirectAttributes.addFlashAttribute("success", "Venta guardada exitosamente");
            return "redirect:/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar venta: " + e.getMessage());
            return "redirect:/ventas/nueva";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarVentaForm(@PathVariable Long id, Model model) {
        try {
            Venta venta = ventaService.findById(id);
            model.addAttribute("venta", venta);
            return "ventas/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Venta no encontrada");
            return "redirect:/ventas";
        }
    }

    @GetMapping("/hoy")
    public String ventasHoy(Model model) {
        try {
            List<Venta> ventas = ventaService.findVentasByFecha(LocalDate.now());
            model.addAttribute("ventas", ventas);
            model.addAttribute("fecha", LocalDate.now());
            return "ventas/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar ventas de hoy: " + e.getMessage());
            return "ventas/lista";
        }
    }

    @GetMapping("/rango")
    public String ventasPorRango(@RequestParam LocalDate inicio, 
                               @RequestParam LocalDate fin, 
                               Model model) {
        try {
            List<Venta> ventas = ventaService.findVentasByRangoFecha(inicio, fin);
            model.addAttribute("ventas", ventas);
            model.addAttribute("fechaInicio", inicio);
            model.addAttribute("fechaFin", fin);
            return "ventas/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar ventas: " + e.getMessage());
            return "ventas/lista";
        }
    }

    @PostMapping("/confirmar/{id}")
    public String confirmarVenta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.confirmarVenta(id);
            redirectAttributes.addFlashAttribute("success", "Venta confirmada exitosamente");
            return "redirect:/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al confirmar venta: " + e.getMessage());
            return "redirect:/ventas";
        }
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarVenta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.cancelarVenta(id);
            redirectAttributes.addFlashAttribute("success", "Venta cancelada exitosamente");
            return "redirect:/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar venta: " + e.getMessage());
            return "redirect:/ventas";
        }
    }

    @GetMapping("/detalle/{id}")
    public String detalleVenta(@PathVariable Long id, Model model) {
        try {
            Venta venta = ventaService.findById(id);
            model.addAttribute("venta", venta);
            return "ventas/detalle";
        } catch (Exception e) {
            model.addAttribute("error", "Venta no encontrada");
            return "redirect:/ventas";
        }
    }
}
