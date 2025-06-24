package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/inventario")
@Transactional
public class InventarioCompletoController {

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
            String codigoBarras = generarCodigoBarras();
            model.addAttribute("codigoBarrasGenerado", codigoBarras);
            return "inventario/formulario-completo";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/guardar-completo")
    @Transactional
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
            // 1. Crear o buscar categoría (TRG_CATEGORIAS_BI asignará ID automáticamente)
            Categoria categoria = categoriaRepository.findByNombre(categoriaNombre);
            if (categoria == null) {
                categoria = new Categoria();
                categoria.setNombre(categoriaNombre);
                categoria.setDescripcion(categoriaDescripcion);
                categoria = categoriaRepository.save(categoria); // ID automático por trigger
            }
            
            // 2. Crear o buscar marca (TRG_MARCAS_BI asignará ID automáticamente)
            Marca marca = marcaRepository.findByNombre(marcaNombre);
            if (marca == null) {
                marca = new Marca();
                marca.setNombre(marcaNombre);
                marca = marcaRepository.save(marca); // ID automático por trigger
            }
            
            // 3. Crear producto (TRG_PRODUCTOS_BI asignará ID automáticamente)
            Producto producto = new Producto(
                productoNombre,
                productoDescripcion,
                productoCodigoBarras,
                categoria.getIdCategoria(),
                marca.getIdMarca(),
                productoStockMinimo
            );
            // NO establecer ID manualmente - dejar que el trigger lo haga
            producto = productoService.save(producto); // El trigger asignará el ID
            
            // 4. Crear o buscar proveedor (TRG_PROVEEDORES_BI asignará ID automáticamente)
            Proveedor proveedor = proveedorRepository.findByNombre(proveedorNombre);
            if (proveedor == null) {
                proveedor = new Proveedor();
                proveedor.setNombre(proveedorNombre);
                proveedor.setTelefono(proveedorTelefono);
                proveedor.setDireccion(proveedorDireccion);
                proveedor = proveedorRepository.save(proveedor); // ID automático por trigger
            }
            
            // 5. Crear lote de inventario (TRG_LOTES_INVENTARIO_BI asignará ID automáticamente)
            // El trigger TRG_UPDATE_STOCK_ACTUAL actualizará automáticamente el stock del producto
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
            
            lote = loteInventarioService.save(lote); // Triggers manejarán todo automáticamente
            
            redirectAttributes.addFlashAttribute("success", 
                "¡Producto e inventario creados exitosamente! " +
                "Producto: " + producto.getNombre() + " (ID: " + producto.getIdProducto() + "), " +
                "Lote: " + lote.getIdLote() + ", " +
                "Cantidad: " + loteCantidad + " unidades. " +
                "Los triggers de Oracle han actualizado automáticamente el stock y generado notificaciones si es necesario.");
            
            return "redirect:/dashboard";
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar: " + e.getMessage() + 
                ". Verifica que todos los campos estén completos y que no haya códigos de barras duplicados.");
            return "redirect:/inventario/nuevo-completo";
        }
    }

    @GetMapping("/agregar-stock")
    public String mostrarFormularioStock(Model model) {
        try {
            List<Producto> productos = productoService.findAll();
            List<Proveedor> proveedores = proveedorRepository.findAll();
            
            model.addAttribute("productos", productos);
            model.addAttribute("proveedores", proveedores);
            
            return "inventario/formulario-stock";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/guardar-stock")
    @Transactional
    public String guardarStock(
            @RequestParam("productoId") Long productoId,
            @RequestParam("proveedorId") Long proveedorId,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("precioCompra") Double precioCompra,
            @RequestParam("precioVenta") Double precioVenta,
            @RequestParam(value = "fechaCaducidad", required = false) String fechaCaducidad,
            
            RedirectAttributes redirectAttributes) {
        
        try {
            Producto producto = productoService.findById(productoId);
            Proveedor proveedor = proveedorRepository.findById(proveedorId).orElse(null);
            
            if (producto == null || proveedor == null) {
                redirectAttributes.addFlashAttribute("error", "Producto o proveedor no encontrado");
                return "redirect:/inventario/agregar-stock";
            }
            
            // Crear nuevo lote - los triggers manejarán todo automáticamente
            LoteInventario lote = new LoteInventario();
            lote.setProducto(producto);
            lote.setProveedor(proveedor);
            lote.setCantidad(cantidad);
            lote.setPrecioCompra(BigDecimal.valueOf(precioCompra));
            lote.setPrecioVenta(BigDecimal.valueOf(precioVenta));
            lote.setFechaIngreso(LocalDate.now());
            
            if (fechaCaducidad != null && !fechaCaducidad.trim().isEmpty()) {
                lote.setFechaCaducidad(LocalDate.parse(fechaCaducidad));
            }
            
            lote = loteInventarioService.save(lote);
            
            redirectAttributes.addFlashAttribute("success", 
                "¡Stock agregado exitosamente! " +
                "Producto: " + producto.getNombre() + ", " +
                "Cantidad agregada: " + cantidad + " unidades, " +
                "Lote: " + lote.getIdLote() + ". " +
                "El stock se ha actualizado automáticamente por los triggers de Oracle.");
            
            return "redirect:/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al agregar stock: " + e.getMessage());
            return "redirect:/inventario/agregar-stock";
        }
    }

    private String generarCodigoBarras() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "CB" + timestamp + String.format("%03d", random);
    }
}
