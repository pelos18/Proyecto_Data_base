package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("marcas", marcaRepository.findAll());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, 
                                RedirectAttributes redirectAttributes) {
        try {
            // Validar código de barras único
            if (producto.getIdProducto() == null && 
                productoRepository.findByCodigoBarras(producto.getCodigoBarras()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Ya existe un producto con ese código de barras");
                return "redirect:/api/productos/nuevo";
            }

            producto.setActivo(true);
            productoRepository.save(producto);
            
            redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
            return "redirect:/api/productos";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar producto: " + e.getMessage());
            return "redirect:/api/productos/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarProductoForm(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("marcas", marcaRepository.findAll());
        return "productos/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            producto.setActivo(false);
            productoRepository.save(producto);
            
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/api/productos";
    }
}
