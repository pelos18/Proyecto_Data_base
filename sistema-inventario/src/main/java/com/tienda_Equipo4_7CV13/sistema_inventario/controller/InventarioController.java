package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private ProductoService productoService;
    
    @Autowired
    private LoteInventarioService loteInventarioService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String dashboard(Model model, Authentication auth) {
        try {
            // Obtener información del usuario autenticado
            String username = auth.getName();
            Usuario usuario = usuarioService.findByUsuario(username);
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("totalProductos", productoService.countProductosActivos());
            model.addAttribute("stockBajo", productoService.countProductosStockBajo());
            model.addAttribute("lotesActivos", loteInventarioService.countLotesActivos());
            model.addAttribute("lotesVencer", loteInventarioService.countLotesProximosAVencer(30));
            
            return "inventario/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar dashboard: " + e.getMessage());
            return "error/500";
        }
    }

    // === GESTIÓN DE PRODUCTOS ===
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "inventario/productos/lista";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("marcas", marcaService.findAll());
        return "inventario/productos/formulario";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, 
                                RedirectAttributes redirectAttributes,
                                Authentication auth) {
        try {
            // Validar que no exista el código de barras
            if (producto.getIdProducto() == null && 
                productoService.existsByCodigoBarras(producto.getCodigoBarras())) {
                redirectAttributes.addFlashAttribute("error", 
                    "Ya existe un producto con ese código de barras");
                return "redirect:/inventario/productos/nuevo";
            }

            // Establecer valores por defecto
            if (producto.getStockActual() == null) producto.setStockActual(0L);
            if (producto.getStockMinimo() == null) producto.setStockMinimo(5L);
            producto.setActivo(1L);

            // Establecer IDs manualmente para evitar error de persistencia
            if (producto.getIdProducto() == null) {
                // El trigger se encargará del ID
            }

            productoService.save(producto);
            
            redirectAttributes.addFlashAttribute("success", 
                "Producto guardado exitosamente. ID generado: " + producto.getIdProducto());
            return "redirect:/inventario/productos";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar producto: " + e.getMessage());
            return "redirect:/inventario/productos/nuevo";
        }
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProductoForm(@PathVariable Long id, Model model) {
        try {
            Producto producto = productoService.findById(id);
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.findAll());
            model.addAttribute("marcas", marcaService.findAll());
            return "inventario/productos/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Producto no encontrado");
            return "redirect:/inventario/productos";
        }
    }

    // === GESTIÓN DE LOTES ===
    @GetMapping("/lotes")
    public String listarLotes(Model model) {
        model.addAttribute("lotes", loteInventarioService.findAll());
        return "inventario/lotes/lista";
    }

    @GetMapping("/lotes/nuevo")
    public String nuevoLoteForm(Model model) {
        model.addAttribute("lote", new LoteInventario());
        model.addAttribute("productos", productoService.findProductosActivos());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "inventario/lotes/formulario";
    }

    @PostMapping("/lotes/guardar")
    public String guardarLote(@ModelAttribute LoteInventario lote,
                            @RequestParam("fechaCaducidadStr") String fechaCaducidadStr,
                            RedirectAttributes redirectAttributes,
                            Authentication auth) {
        try {
            // Establecer fecha de ingreso actual
            lote.setFechaIngreso(LocalDate.now());
            
            // Convertir fecha de caducidad si se proporciona
            if (fechaCaducidadStr != null && !fechaCaducidadStr.isEmpty()) {
                lote.setFechaCaducidad(LocalDate.parse(fechaCaducidadStr));
            }

            // Validaciones básicas
            if (lote.getCantidad() == null || lote.getCantidad() <= 0) {
                redirectAttributes.addFlashAttribute("error", "La cantidad debe ser mayor a 0");
                return "redirect:/inventario/lotes/nuevo";
            }

            if (lote.getPrecioCompra() == null || lote.getPrecioCompra().compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "El precio de compra debe ser mayor a 0");
                return "redirect:/inventario/lotes/nuevo";
            }

            if (lote.getPrecioVenta() == null || lote.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "El precio de venta debe ser mayor a 0");
                return "redirect:/inventario/lotes/nuevo";
            }

            // El trigger TRG_RESTRICT_PRECIOS validará los permisos automáticamente
            loteInventarioService.save(lote);
            
            redirectAttributes.addFlashAttribute("success", 
                "Lote guardado exitosamente. ID generado: " + lote.getIdLote());
            return "redirect:/inventario/lotes";
            
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("Solo los roles")) {
                redirectAttributes.addFlashAttribute("error", 
                    "No tienes permisos para modificar precios. Solo usuarios 'dueño' y 'administrativo' pueden hacerlo.");
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "Error al guardar lote: " + errorMsg);
            }
            return "redirect:/inventario/lotes/nuevo";
        }
    }

    @GetMapping("/lotes/editar/{id}")
    public String editarLoteForm(@PathVariable Long id, Model model) {
        try {
            LoteInventario lote = loteInventarioService.findById(id);
            model.addAttribute("lote", lote);
            model.addAttribute("productos", productoService.findProductosActivos());
            model.addAttribute("proveedores", proveedorRepository.findAll());
            return "inventario/lotes/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Lote no encontrado");
            return "redirect:/inventario/lotes";
        }
    }

    // === GESTIÓN DE CATEGORÍAS ===
    @GetMapping("/categorias")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "inventario/categorias/lista";
    }

    @GetMapping("/categorias/nueva")
    public String nuevaCategoriaForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "inventario/categorias/formulario";
    }

    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria, 
                                 RedirectAttributes redirectAttributes) {
        try {
            categoriaService.save(categoria);
            redirectAttributes.addFlashAttribute("success", 
                "Categoría guardada exitosamente. ID generado: " + categoria.getIdCategoria());
            return "redirect:/inventario/categorias";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar categoría: " + e.getMessage());
            return "redirect:/inventario/categorias/nueva";
        }
    }

    // === GESTIÓN DE MARCAS ===
    @GetMapping("/marcas")
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcaService.findAll());
        return "inventario/marcas/lista";
    }

    @GetMapping("/marcas/nueva")
    public String nuevaMarcaForm(Model model) {
        model.addAttribute("marca", new Marca());
        return "inventario/marcas/formulario";
    }

    @PostMapping("/marcas/guardar")
    public String guardarMarca(@ModelAttribute Marca marca, 
                             RedirectAttributes redirectAttributes) {
        try {
            marcaService.save(marca);
            redirectAttributes.addFlashAttribute("success", 
                "Marca guardada exitosamente. ID generado: " + marca.getIdMarca());
            return "redirect:/inventario/marcas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar marca: " + e.getMessage());
            return "redirect:/inventario/marcas/nueva";
        }
    }

    // === REPORTES ===
    @GetMapping("/reportes")
    public String reportes(Model model) {
        try {
            model.addAttribute("productosStockBajo", productoService.findProductosConStockBajo());
            model.addAttribute("lotesProximosVencer", loteInventarioService.findLotesProximosACaducar(30));
            model.addAttribute("lotesCaducados", loteInventarioService.findLotesCaducados());
            model.addAttribute("valorTotalInventario", loteInventarioService.getValorTotalInventario());
            
            return "inventario/reportes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar reportes: " + e.getMessage());
            return "inventario/dashboard";
        }
    }
}
