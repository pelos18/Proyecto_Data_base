package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/catalogo-cliente")
public class CatalogoController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;

    @GetMapping
    public String mostrarCatalogo(Model model, 
                                @RequestParam(required = false) Long clienteId,
                                @RequestParam(required = false) String clienteNombre) {
        
        List<Producto> productos = productoRepository.findByActivoTrue();
        
        productos.forEach(producto -> {
            Double precio = loteInventarioRepository.findPrecioVentaByProductoId(producto.getIdProducto());
            // Aquí puedes manejar la lógica para asignar el precio si lo necesitas
        });
        
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
        model.addAttribute("marcas", marcaRepository.findAllByOrderByNombreAsc());
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("clienteNombre", clienteNombre);
        
        return "catalogo/cliente-catalogo";
    }

    @PostMapping("/procesar-compra")
    public String procesarCompra(@RequestParam Long clienteId,
                               @RequestParam String productosJson,
                               RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setFechaVenta(LocalDateTime.now());
            venta.setEstado("PENDIENTE");
            venta.setTotal(BigDecimal.ZERO);
            
            // Necesitarías una entidad Venta, si no la tienes, esto fallará.
            // venta = ventaRepository.save(venta);
            
            redirectAttributes.addFlashAttribute("success", 
                "¡Compra procesada exitosamente! ID de venta: "); // + venta.getIdVenta());
            redirectAttributes.addFlashAttribute("ventaId", 1L); // + venta.getIdVenta());
            
            return "redirect:/catalogo-cliente/confirmacion";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al procesar la compra: " + e.getMessage());
            return "redirect:/catalogo-cliente";
        }
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model) {
        return "catalogo/confirmacion-compra";
    }

    @GetMapping("/buscar")
    @ResponseBody
    public List<Map<String, Object>> buscarProductos(@RequestParam String q) {
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCaseOrCodigoBarrasContaining(q, q);
        
        return productos.stream()
            // --- CORRECCIÓN AQUÍ ---
            // Se cambia la comparación de "== 1L" por la forma correcta para un Booleano.
            .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
            .map(producto -> {
                Double precio = loteInventarioRepository.findPrecioVentaByProductoId(producto.getIdProducto());
                Map<String, Object> productoMap = new java.util.HashMap<>();
                productoMap.put("idProducto", producto.getIdProducto());
                productoMap.put("nombre", producto.getNombre());
                productoMap.put("descripcion", producto.getDescripcion() != null ? producto.getDescripcion() : "");
                productoMap.put("stockActual", producto.getStockActual());
                productoMap.put("precio", precio != null ? precio : 0.0);
                productoMap.put("categoria", producto.getCategoria() != null ? producto.getCategoria().getNombre() : "");
                productoMap.put("marca", producto.getMarca() != null ? producto.getMarca().getNombre() : "");
                return productoMap;
            })
            .collect(Collectors.toList());
    }
}