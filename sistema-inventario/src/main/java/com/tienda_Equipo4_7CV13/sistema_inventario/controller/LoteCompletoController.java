package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.math.BigDecimal;

@Controller
@RequestMapping("/inventario")
public class LoteCompletoController {

    @Autowired
    private LoteInventarioService loteInventarioService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/nuevo-completo")
    public String mostrarFormularioCompleto(Model model) {
        try {
            model.addAttribute("lote", new LoteInventario());
            model.addAttribute("producto", new Producto());
            model.addAttribute("proveedor", new Proveedor());
            model.addAttribute("categoria", new Categoria());
            model.addAttribute("marca", new Marca());
            
            // Generar código de barras automático
            String codigoBarras = "CB" + System.currentTimeMillis();
            model.addAttribute("codigoBarrasGenerado", codigoBarras);
            
            return "lotes/formulario-completo";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/guardar-completo")
    public String guardarCompleto(
            // Datos del producto
            @RequestParam("productoNombre") String productoNombre,
            @RequestParam("productoDescripcion") String productoDescripcion,
            @RequestParam("productoCodigoBarras") String productoCodigoBarras,
            @RequestParam("productoStockMinimo") Long productoStockMinimo,
            
            // Datos de categoría
            @RequestParam("categoriaNombre") String categoriaNombre,
            @RequestParam(value = "categoriaDescripcion", required = false) String categoriaDescripcion,
            
            // Datos de marca
            @RequestParam("marcaNombre") String marcaNombre,
            
            // Datos del proveedor
            @RequestParam("proveedorNombre") String proveedorNombre,
            @RequestParam(value = "proveedorTelefono", required = false) String proveedorTelefono,
            @RequestParam(value = "proveedorDireccion", required = false) String proveedorDireccion,
            
            // Datos del lote
            @RequestParam("loteCantidad") Integer loteCantidad,
            @RequestParam("lotePrecioCompra") Double lotePrecioCompra,
            @RequestParam("lotePrecioVenta") Double lotePrecioVenta,
            @RequestParam(value = "loteFechaCaducidad", required = false) String loteFechaCaducidad,
            
            RedirectAttributes redirectAttributes) {
        
        try {
            // 1. Crear o buscar categoría
            Categoria categoria = categoriaRepository.findByNombre(categoriaNombre);
            if (categoria == null) {
                categoria = new Categoria();
                categoria.setNombre(categoriaNombre);
                categoria.setDescripcion(categoriaDescripcion);
                categoria = categoriaRepository.save(categoria);
            }
            
            // 2. Crear o buscar marca
            Marca marca = marcaRepository.findByNombre(marcaNombre);
            if (marca == null) {
                marca = new Marca();
                marca.setNombre(marcaNombre);
                marca = marcaRepository.save(marca);
            }
            
            // 3. Crear producto
            Producto producto = new Producto();
            producto.setNombre(productoNombre);
            producto.setDescripcion(productoDescripcion);
            producto.setCodigoBarras(productoCodigoBarras);
            producto.setCategoria(categoria);
            producto.setMarca(marca);
            producto.setStockMinimo(productoStockMinimo);
            producto.setStockActual(0L); // Se actualizará automáticamente con trigger
            producto.setActivo(1L);
            producto = productoService.save(producto);
            
            // 4. Crear o buscar proveedor
            Proveedor proveedor = proveedorRepository.findByNombre(proveedorNombre);
            if (proveedor == null) {
                proveedor = new Proveedor();
                proveedor.setNombre(proveedorNombre);
                proveedor.setTelefono(proveedorTelefono);
                proveedor.setDireccion(proveedorDireccion);
                proveedor = proveedorRepository.save(proveedor);
            }
            
            // 5. Crear lote de inventario
            LoteInventario lote = new LoteInventario();
            lote.setProducto(producto);
            lote.setProveedor(proveedor);
            lote.setCantidad(loteCantidad);
            lote.setPrecioCompra(BigDecimal.valueOf(lotePrecioCompra));
            lote.setPrecioVenta(BigDecimal.valueOf(lotePrecioVenta));
            lote.setFechaIngreso(LocalDate.now());
            
            if (loteFechaCaducidad != null && !loteFechaCaducidad.trim().isEmpty()) {
                lote.setFechaCaducidad(LocalDate.parse(loteFechaCaducidad));
            }
            
            lote = loteInventarioService.save(lote);
            
            redirectAttributes.addFlashAttribute("success", 
                "¡Inventario creado exitosamente! " +
                "Producto: " + producto.getNombre() + " (ID: " + producto.getIdProducto() + "), " +
                "Lote: " + lote.getIdLote() + ", " +
                "Stock actualizado automáticamente por triggers de Oracle.");
            
            return "redirect:/dashboard";
            
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("restrict_precios")) {
                redirectAttributes.addFlashAttribute("error", 
                    "Error de permisos: Solo usuarios 'dueño' y 'administrativo' pueden establecer precios.");
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "Error al guardar: " + errorMsg);
            }
            return "redirect:/inventario/nuevo-completo";
        }
    }
}
