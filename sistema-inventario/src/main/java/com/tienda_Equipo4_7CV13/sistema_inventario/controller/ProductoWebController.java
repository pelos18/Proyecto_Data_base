package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Categoria;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Marca;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.CategoriaService;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.MarcaService;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoWebController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public String listarProductos(Model model) {
        try {
            List<Producto> productos = productoService.findAll();
            model.addAttribute("productos", productos);
            return "productos/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar productos: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            List<Marca> marcas = marcaService.findAll();
            
            model.addAttribute("producto", new Producto());
            model.addAttribute("categorias", categorias);
            model.addAttribute("marcas", marcas);
            model.addAttribute("accion", "Crear");
            
            return "productos/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Producto producto = productoService.findById(id);
            if (producto == null) {
                return "redirect:/productos?error=Producto no encontrado";
            }
            
            List<Categoria> categorias = categoriaService.findAll();
            List<Marca> marcas = marcaService.findAll();
            
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categorias);
            model.addAttribute("marcas", marcas);
            model.addAttribute("accion", "Editar");
            
            return "productos/formulario";
        } catch (Exception e) {
            return "redirect:/productos?error=Error al cargar producto";
        }
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, 
                                  RedirectAttributes redirectAttributes) {
        try {
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre es obligatorio");
                return "redirect:/productos/nuevo";
            }
            
            if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
                redirectAttributes.addFlashAttribute("error", "La categoría es obligatoria");
                return "redirect:/productos/nuevo";
            }
            
            if (producto.getMarca() == null || producto.getMarca().getIdMarca() == null) {
                redirectAttributes.addFlashAttribute("error", "La marca es obligatoria");
                return "redirect:/productos/nuevo";
            }

            if (producto.getStockMinimo() == null) {
                producto.setStockMinimo(0L);
            }
            if (producto.getStockActual() == null) {
                producto.setStockActual(0L);
            }
            if (producto.getActivo() == null) {
                producto.setActivo(true);
            }

            productoService.save(producto);
            redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
            return "redirect:/productos";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar producto: " + e.getMessage());
            return "redirect:/productos/nuevo";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.findById(id);
            if (producto != null) {
                productoService.delete(id);
                redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}