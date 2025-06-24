package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface InventarioService {
    
    /**
     * Crea un producto simple con categoría y marca
     */
    Producto crearProductoSimple(String nombreProducto, String descripcion, String nombreCategoria, String nombreMarca);
    
    /**
     * Crea un lote para un producto existente (maneja el orden correcto de guardado)
     */
    void crearLoteParaProductoExistente(Long productoId, String nombreProveedor, Integer cantidad, 
                                       BigDecimal precioCompra, BigDecimal precioVenta, LocalDate fechaCaducidad);
    
    /**
     * Busca un producto por ID
     */
    Producto findProductoById(Long productoId);
    
    /**
     * Método completo que combina crear producto y lote
     */
    default Producto crearProductoConLote(String nombreProducto, String descripcion, String nombreCategoria, 
                                         String nombreMarca, String nombreProveedor, Integer cantidad,
                                         BigDecimal precioCompra, BigDecimal precioVenta, LocalDate fechaCaducidad) {
        
        // 1. Crear producto primero
        Producto producto = crearProductoSimple(nombreProducto, descripcion, nombreCategoria, nombreMarca);
        
        // 2. Crear lote para ese producto (maneja el orden correcto)
        crearLoteParaProductoExistente(producto.getIdProducto(), nombreProveedor, cantidad, 
                                     precioCompra, precioVenta, fechaCaducidad);
        
        return producto;
    }
}
