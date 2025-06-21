package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

/**
 * Excepción lanzada cuando no se encuentra un producto
 */
public class ProductoNotFoundException extends SystemException {
    
    public ProductoNotFoundException(Long id) {
        super("PRODUCTO_NOT_FOUND", "Producto con ID " + id + " no encontrado");
    }
    
    public ProductoNotFoundException(String codigoBarras) {
        super("PRODUCTO_NOT_FOUND", "Producto con código de barras " + codigoBarras + " no encontrado");
    }
    
    public ProductoNotFoundException(String message, Throwable cause) {
        super("PRODUCTO_NOT_FOUND", message, cause);
    }
}
