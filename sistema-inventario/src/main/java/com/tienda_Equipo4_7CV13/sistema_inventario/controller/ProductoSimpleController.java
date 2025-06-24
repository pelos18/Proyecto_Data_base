package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos-simple")
public class ProductoSimpleController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public String listar(Model model) {
        try {
            List<Producto> productos = productoRepository.findAll();
            model.addAttribute("productos", productos);
            model.addAttribute("mensaje", "Productos cargados: " + productos.size());
            return "productos-simple/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar productos: " + e.getMessage());
            return "productos-simple/lista";
        }
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos-simple/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            // Establecer valores por defecto según tu estructura Oracle
            if (producto.getActivo() == null) {
                // --- CORRECCIÓN AQUÍ ---
                // Se cambia la asignación de "1L" por el valor booleano "true".
                producto.setActivo(true);
            }
            if (producto.getStockMinimo() == null) {
                producto.setStockMinimo(5L);
            }
            if (producto.getStockActual() == null) {
                producto.setStockActual(0L);
            }

            // El trigger TRG_PRODUCTOS_BI generará el ID automáticamente
            producto.setIdProducto(null);

            Producto guardado = productoRepository.save(producto);
            
            redirectAttributes.addFlashAttribute("success", 
                "Producto guardado exitosamente con ID: " + guardado.getIdProducto());
            
            return "redirect:/productos-simple";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar: " + e.getMessage());
            return "redirect:/productos-simple/nuevo";
        }
    }
}