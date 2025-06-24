package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/inventario")
@PreAuthorize("hasAnyRole('DUEÑO', 'ADMINISTRATIVO')")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Redirección para la URL antigua, por si acaso
    @GetMapping("/nuevo-completo")
    public String redireccionarANuevoProducto() {
        return "redirect:/inventario/nuevo-producto";
    }

    // PASO 1: MUESTRA EL FORMULARIO DE PRODUCTO
    @GetMapping("/nuevo-producto")
    public String mostrarFormularioProducto(Model model) {
        model.addAttribute("producto", new Producto());
        // --- CORRECCIÓN AQUÍ ---
        // Apuntamos al nombre de tu archivo HTML real
        return "inventario/formulario-completo"; 
    }

    // PASO 2: GUARDA EL PRODUCTO Y REDIRIGE
    @PostMapping("/guardar-producto")
    public String guardarProducto(@RequestParam String nombre,
                                  @RequestParam String descripcion,
                                  @RequestParam String nombreCategoria,
                                  @RequestParam String nombreMarca,
                                  RedirectAttributes redirectAttributes) {
        try {
            Producto productoGuardado = inventarioService.crearProductoSimple(nombre, descripcion, nombreCategoria, nombreMarca);
            return "redirect:/inventario/producto/" + productoGuardado.getIdProducto() + "/nuevo-lote";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
            return "redirect:/inventario/nuevo-producto";
        }
    }

    // PASO 3: MUESTRA EL FORMULARIO DE LOTE
    @GetMapping("/producto/{productoId}/nuevo-lote")
    public String mostrarFormularioLote(@PathVariable Long productoId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = inventarioService.findProductoById(productoId);
            model.addAttribute("producto", producto);
            // Esto asume que tienes el archivo en: templates/inventario/lotes/formulario.html
            return "inventario/lotes/formulario"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar producto para el lote: " + e.getMessage());
            return "redirect:/inventario/nuevo-producto";
        }
    }

    // PASO 4: GUARDA EL LOTE Y TERMINA
    @PostMapping("/guardar-lote")
    public String guardarLote(@RequestParam Long productoId,
                              @RequestParam String nombreProveedor,
                              @RequestParam Integer cantidad,
                              @RequestParam BigDecimal precioCompra,
                              @RequestParam BigDecimal precioVenta,
                              @RequestParam(required = false) LocalDate fechaCaducidad,
                              RedirectAttributes redirectAttributes) {
        try {
            inventarioService.crearLoteParaProductoExistente(
                productoId, nombreProveedor, cantidad, precioCompra, precioVenta, fechaCaducidad
            );
            redirectAttributes.addFlashAttribute("success", "¡Producto y Lote creados exitosamente!");
            return "redirect:/productos"; // O a la vista de tu lista de productos principal
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el lote: " + e.getMessage());
            return "redirect:/inventario/producto/" + productoId + "/nuevo-lote";
        }
    }
}