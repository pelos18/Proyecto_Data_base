package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;

    @Override
    @Transactional
    public Producto crearProductoSimple(String nombreProducto, String descripcion, String nombreCategoria, String nombreMarca) {
        Categoria categoria = categoriaRepository.findByNombre(nombreCategoria).orElseGet(() -> new Categoria(nombreCategoria, ""));
        Marca marca = marcaRepository.findByNombre(nombreMarca).orElseGet(() -> new Marca(nombreMarca));

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombreProducto);
        nuevoProducto.setDescripcion(descripcion);
        nuevoProducto.setActivo(true);
        nuevoProducto.setStockMinimo(0L);
        nuevoProducto.setStockActual(0L);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setMarca(marca);

        return productoRepository.save(nuevoProducto);
    }

    @Override
    @Transactional
    public void crearLoteParaProductoExistente(Long productoId, String nombreProveedor, Integer cantidad, BigDecimal precioCompra, BigDecimal precioVenta, LocalDate fechaCaducidad) {
        Producto producto = findProductoById(productoId);
        
        // --- AQUÍ SE IMPLEMENTA TU IDEA: GUARDAR EL PROVEEDOR PRIMERO ---
        Optional<Proveedor> proveedorOpt = proveedorRepository.findByNombre(nombreProveedor);
        Proveedor proveedor;

        if (proveedorOpt.isPresent()) {
            proveedor = proveedorOpt.get();
        } else {
            // Si el proveedor no existe, se crea y se GUARDA INMEDIATAMENTE
            // para que la base de datos le asigne un ID.
            Proveedor nuevoProv = new Proveedor();
            nuevoProv.setNombre(nombreProveedor);
            proveedor = proveedorRepository.save(nuevoProv);
        }
        // ----------------------------------------------------------------

        // Ahora, con el proveedor ya guardado, creamos y guardamos el lote
        LoteInventario nuevoLote = new LoteInventario();
        nuevoLote.setProducto(producto);
        nuevoLote.setProveedor(proveedor); // Usamos el proveedor que ya tiene un ID garantizado
        nuevoLote.setCantidad(cantidad);
        nuevoLote.setPrecioCompra(precioCompra);
        nuevoLote.setPrecioVenta(precioVenta);
        nuevoLote.setFechaCaducidad(fechaCaducidad);

        loteInventarioRepository.save(nuevoLote);
        
        // El trigger se encarga del stock, pero si no, esta es la lógica:
        // long stockActual = (producto.getStockActual() != null ? producto.getStockActual() : 0L) + cantidad;
        // producto.setStockActual(stockActual);
        // productoRepository.save(producto);
    }
    
    @Override
    public Producto findProductoById(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));
    }
}