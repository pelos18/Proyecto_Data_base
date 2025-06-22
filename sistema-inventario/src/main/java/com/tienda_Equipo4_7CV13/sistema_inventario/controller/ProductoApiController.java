package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;

    @GetMapping("/disponibles")
    public ResponseEntity<List<Map<String, Object>>> getProductosDisponibles() {
        try {
            List<Producto> productos = productoRepository.findByActivoTrue();
            List<Map<String, Object>> productosConPrecio = productos.stream()
                .map(producto -> {
                    Map<String, Object> productoMap = new HashMap<>();
                    productoMap.put("idProducto", producto.getIdProducto());
                    productoMap.put("nombre", producto.getNombre());
                    productoMap.put("descripcion", producto.getDescripcion());
                    productoMap.put("stockActual", producto.getStockActual());
                    
                    // Obtener precio del lote más reciente
                    Double precio = loteInventarioRepository.findPrecioVentaByProductoId(producto.getIdProducto());
                    productoMap.put("precio", precio != null ? precio : 0.0);
                    
                    return productoMap;
                })
                .toList();
                
            return ResponseEntity.ok(productosConPrecio);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Map<String, Object>>> buscarProductos(@RequestParam String q) {
        try {
            List<Producto> productos = productoRepository.findByNombreContainingIgnoreCaseOrCodigoBarrasContaining(q, q);
            List<Map<String, Object>> productosConPrecio = productos.stream()
                .map(producto -> {
                    Map<String, Object> productoMap = new HashMap<>();
                    productoMap.put("idProducto", producto.getIdProducto());
                    productoMap.put("nombre", producto.getNombre());
                    productoMap.put("codigoBarras", producto.getCodigoBarras());
                    productoMap.put("stockActual", producto.getStockActual());
                    
                    // Obtener precio del lote más reciente
                    Double precio = loteInventarioRepository.findPrecioVentaByProductoId(producto.getIdProducto());
                    productoMap.put("precio", precio != null ? precio : 0.0);
                    
                    return productoMap;
                })
                .toList();
                
            return ResponseEntity.ok(productosConPrecio);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Producto>> getProductosAdmin() {
        try {
            List<Producto> productos = productoRepository.findAll();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
