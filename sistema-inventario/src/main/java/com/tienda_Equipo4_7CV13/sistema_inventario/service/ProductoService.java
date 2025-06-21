package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.ProductoNotFoundException;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.ValidationException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private NotificacionService notificacionService;
    
    // Obtener todos los productos
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
    
    // Obtener productos activos
    public List<Producto> findProductosActivos() {
        return productoRepository.findByActivoTrue();
    }
    
    // Obtener producto por ID
    public Producto findById(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));
    }
    
    // Buscar producto por código de barras
    public Producto findByCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras)
            .orElseThrow(() -> new ProductoNotFoundException(codigoBarras));
    }
    
    // Crear nuevo producto
    public Producto save(Producto producto) {
        // Validar código de barras único si se proporciona
        if (producto.getCodigoBarras() != null && !producto.getCodigoBarras().isEmpty()) {
            if (productoRepository.existsByCodigoBarras(producto.getCodigoBarras())) {
                throw new ValidationException("Ya existe un producto con el código de barras: " + producto.getCodigoBarras());
            }
        }
        
        return productoRepository.save(producto);
    }
    
    // Actualizar producto
    public Producto update(Producto producto) {
        Producto productoExistente = findById(producto.getIdProducto());
        
        // Verificar código de barras único si cambió
        if (producto.getCodigoBarras() != null && !producto.getCodigoBarras().isEmpty() &&
            !producto.getCodigoBarras().equals(productoExistente.getCodigoBarras()) &&
            productoRepository.existsByCodigoBarras(producto.getCodigoBarras())) {
            throw new ValidationException("Ya existe un producto con el código de barras: " + producto.getCodigoBarras());
        }
        
        Producto productoActualizado = productoRepository.save(producto);
        
        // Verificar stock bajo después de actualizar
        verificarStockBajo(productoActualizado);
        
        return productoActualizado;
    }
    
    // Eliminar producto (desactivar)
    public void deleteById(Long id) {
        Producto producto = findById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
    
    // Activar producto
    public Producto activar(Long id) {
        Producto producto = findById(id);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }
    
    // Buscar productos por nombre o código
    public List<Producto> buscarPorNombreOCodigo(String termino) {
        return productoRepository.buscarPorNombreOCodigo(termino);
    }
    
    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Obtener productos por categoría
    public List<Producto> findPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoriaIdCategoriaAndActivoTrue(idCategoria);
    }
    
    // Obtener productos por marca
    public List<Producto> findPorMarca(Long idMarca) {
        return productoRepository.findByMarcaIdMarcaAndActivoTrue(idMarca);
    }
    
    // Obtener productos con stock bajo
    public List<Producto> findProductosStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }
    
    // Obtener productos sin stock
    public List<Producto> findProductosSinStock() {
        return productoRepository.findProductosSinStock();
    }
    
    // Obtener productos más vendidos
    public List<Object[]> findProductosMasVendidos() {
        return productoRepository.findProductosMasVendidos();
    }
    
    // Actualizar stock del producto
    public void actualizarStock(Long idProducto, Integer nuevoStock) {
        Producto producto = findById(idProducto);
        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);
        
        // Verificar stock bajo
        verificarStockBajo(producto);
    }
    
    // Reducir stock del producto
    public void reducirStock(Long idProducto, Integer cantidad) {
        Producto producto = findById(idProducto);
        
        if (producto.getStockActual() < cantidad) {
            throw new ValidationException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        
        producto.setStockActual(producto.getStockActual() - cantidad);
        productoRepository.save(producto);
        
        // Verificar stock bajo
        verificarStockBajo(producto);
    }
    
    // Aumentar stock del producto
    public void aumentarStock(Long idProducto, Integer cantidad) {
        Producto producto = findById(idProducto);
        producto.setStockActual(producto.getStockActual() + cantidad);
        productoRepository.save(producto);
    }
    
    // Verificar stock bajo y crear notificación si es necesario
    private void verificarStockBajo(Producto producto) {
        if (producto.getStockActual() <= producto.getStockMinimo()) {
            // Crear notificación de stock bajo
            // Esto se implementaría con el servicio de notificaciones
            // notificacionService.crearNotificacionStockBajo(producto);
        }
    }
    
    // Obtener productos con precios
    public List<Object[]> findProductosConPrecios() {
        return productoRepository.findProductosConPrecios();
    }
    
    // Buscar productos por rango de stock
    public List<Producto> findProductosPorRangoStock(Integer stockMinimo, Integer stockMaximo) {
        return productoRepository.findProductosPorRangoStock(stockMinimo, stockMaximo);
    }
}
