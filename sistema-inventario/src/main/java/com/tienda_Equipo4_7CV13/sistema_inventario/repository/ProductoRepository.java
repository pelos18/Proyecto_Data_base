package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar producto por código de barras
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    
    // Buscar productos por nombre (contiene)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos activos
    List<Producto> findByActivoTrue();
    
    // Buscar productos por categoría
    List<Producto> findByCategoriaIdCategoriaAndActivoTrue(Long idCategoria);
    
    // Buscar productos por marca
    List<Producto> findByMarcaIdMarcaAndActivoTrue(Long idMarca);
    
    // Verificar si existe producto con código de barras
    boolean existsByCodigoBarras(String codigoBarras);
    
    // Productos con stock bajo (stock actual <= stock mínimo)
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();
    
    // Productos sin stock
    @Query("SELECT p FROM Producto p WHERE p.stockActual = 0 AND p.activo = true")
    List<Producto> findProductosSinStock();
    
    // Buscar productos por nombre o código de barras
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Producto> buscarPorNombreOCodigo(@Param("termino") String termino);
    
    // Productos más vendidos
    @Query("SELECT p, SUM(dv.cantidad) as totalVendido FROM Producto p " +
           "JOIN LoteInventario l ON p.idProducto = l.producto.idProducto " +
           "JOIN DetalleVenta dv ON l.idLote = dv.lote.idLote " +
           "JOIN Venta v ON dv.venta.idVenta = v.idVenta " +
           "WHERE v.estado = 'ACEPTADA' AND p.activo = true " +
           "GROUP BY p.idProducto ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos();
    
    // Contar productos por categoría
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.idCategoria = :idCategoria AND p.activo = true")
    Long countByCategoriaIdCategoriaAndActivoTrue(@Param("idCategoria") Long idCategoria);
    
    // Contar productos por marca
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.marca.idMarca = :idMarca AND p.activo = true")
    Long countByMarcaIdMarcaAndActivoTrue(@Param("idMarca") Long idMarca);
    
    // Obtener productos con sus precios de venta actuales
    @Query("SELECT p, l.precioVenta FROM Producto p " +
           "JOIN LoteInventario l ON p.idProducto = l.producto.idProducto " +
           "WHERE p.activo = true AND l.cantidad > 0 " +
           "GROUP BY p.idProducto, l.precioVenta " +
           "ORDER BY p.nombre")
    List<Object[]> findProductosConPrecios();
    
    // Productos por rango de stock
    @Query("SELECT p FROM Producto p WHERE p.stockActual BETWEEN :stockMinimo AND :stockMaximo AND p.activo = true")
    List<Producto> findProductosPorRangoStock(@Param("stockMinimo") Integer stockMinimo, 
                                             @Param("stockMaximo") Integer stockMaximo);
}
