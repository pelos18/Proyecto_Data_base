package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import java.math.BigDecimal;
import java.time.LocalDate;

// El "contrato" que define los métodos que el servicio debe tener.
public interface InventarioService {

    // Método para el Paso 1: Crea el producto y devuelve la entidad guardada.
    Producto crearProductoSimple(String nombreProducto, String descripcion, String nombreCategoria, String nombreMarca);

    // Método para el Paso 2: Añade el lote a un producto que ya existe.
    void crearLoteParaProductoExistente(Long productoId, String nombreProveedor, Integer cantidad, BigDecimal precioCompra, BigDecimal precioVenta, LocalDate fechaCaducidad);
    
    // Método de ayuda para encontrar el producto entre los pasos.
    Producto findProductoById(Long productoId);
}