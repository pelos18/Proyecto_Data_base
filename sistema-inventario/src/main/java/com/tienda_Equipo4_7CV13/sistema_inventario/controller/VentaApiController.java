package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ventas")
public class VentaApiController {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/procesar")
    public ResponseEntity<Map<String, Object>> procesarVentaCliente(
            @RequestBody Map<String, Object> ventaData,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByUsuario(username).orElse(null);
            
            if (usuario == null) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear nueva venta
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDateTime.now());
            venta.setUsuario(usuario);
            venta.setEstado("PENDIENTE");
            
            // Calcular total
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) ventaData.get("items");
            BigDecimal total = BigDecimal.ZERO;
            
            for (Map<String, Object> item : items) {
                Integer cantidad = (Integer) item.get("cantidad");
                Double precio = (Double) item.get("precio");
                total = total.add(BigDecimal.valueOf(precio * cantidad));
            }
            
            venta.setTotal(total);
            venta = ventaRepository.save(venta);
            
            // Crear detalles de venta
            for (Map<String, Object> item : items) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                
                Long productoId = Long.valueOf(item.get("id").toString());
                Producto producto = productoRepository.findById(productoId).orElse(null);
                if (producto != null) {
                    detalle.setProducto(producto);
                    detalle.setCantidad((Integer) item.get("cantidad"));
                    detalle.setPrecioUnitario(BigDecimal.valueOf((Double) item.get("precio")));
                    detalle.setSubtotal(BigDecimal.valueOf((Double) item.get("precio") * (Integer) item.get("cantidad")));
                    
                    detalleVentaRepository.save(detalle);
                }
            }
            
            response.put("success", true);
            response.put("ventaId", venta.getIdVenta());
            response.put("message", "Venta procesada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al procesar la venta: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/procesar-cajero")
    public ResponseEntity<Map<String, Object>> procesarVentaCajero(
            @RequestBody Map<String, Object> ventaData,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByUsuario(username).orElse(null);
            
            if (usuario == null) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear nueva venta
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDateTime.now());
            venta.setUsuario(usuario);
            venta.setEstado("COMPLETADA");
            
            // Cliente opcional
            String clienteIdStr = (String) ventaData.get("clienteId");
            if (clienteIdStr != null && !clienteIdStr.isEmpty()) {
                Long clienteId = Long.valueOf(clienteIdStr);
                Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
                venta.setCliente(cliente);
            }
            
            // Calcular total
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) ventaData.get("items");
            BigDecimal total = BigDecimal.ZERO;
            
            for (Map<String, Object> item : items) {
                Integer cantidad = (Integer) item.get("cantidad");
                Double precio = (Double) item.get("precio");
                total = total.add(BigDecimal.valueOf(precio * cantidad));
            }
            
            venta.setTotal(total);
            venta = ventaRepository.save(venta);
            
            // Crear detalles de venta y actualizar stock
            for (Map<String, Object> item : items) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                
                Long productoId = Long.valueOf(item.get("id").toString());
                Producto producto = productoRepository.findById(productoId).orElse(null);
                if (producto != null) {
                    detalle.setProducto(producto);
                    detalle.setCantidad((Integer) item.get("cantidad"));
                    detalle.setPrecioUnitario(BigDecimal.valueOf((Double) item.get("precio")));
                    detalle.setSubtotal(BigDecimal.valueOf((Double) item.get("precio") * (Integer) item.get("cantidad")));
                    
                    detalleVentaRepository.save(detalle);
                    
                    // Actualizar stock del producto
                    producto.setStockActual(producto.getStockActual() - (Integer) item.get("cantidad"));
                    productoRepository.save(producto);
                }
            }
            
            response.put("success", true);
            response.put("ventaId", venta.getIdVenta());
            response.put("message", "Venta procesada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al procesar la venta: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
