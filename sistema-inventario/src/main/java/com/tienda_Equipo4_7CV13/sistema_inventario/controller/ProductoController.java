package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        try {
            List<Producto> productos = productoService.findAll();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        try {
            Producto producto = productoService.findById(id);
            return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Producto> createProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.save(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        try {
            producto.setIdProducto(id);
            Producto productoActualizado = productoService.save(producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> getProductosStockBajo() {
        try {
            List<Producto> productos = productoService.findProductosStockBajo();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String termino) {
        try {
            List<Producto> productos = productoService.buscarPorNombreOCodigo(termino);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
