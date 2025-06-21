package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteInventarioRepository extends JpaRepository<LoteInventario, Long> {
    
    // Buscar lotes por producto
    List<LoteInventario> findByProductoIdProducto(Long idProducto);
    
    // Buscar lotes por proveedor
    List<LoteInventario> findByProveedorIdProveedor(Long idProveedor);
    
    // Buscar lotes con stock disponible
    List<LoteInventario> findByCantidadGreaterThan(Integer cantidad);
    
    // Buscar lotes por producto con stock disponible
    List<LoteInventario> findByProductoIdProductoAndCantidadGreaterThan(Long idProducto, Integer cantidad);
    
    // Lotes próximos a caducar
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaCaducidad IS NOT NULL AND " +
           "l.fechaCaducidad BETWEEN :fechaActual AND :fechaLimite AND l.cantidad > 0")
    List<LoteInventario> findLotesProximosACaducar(@Param("fechaActual") LocalDate fechaActual, 
                                                  @Param("fechaLimite") LocalDate fechaLimite);
    
    // Lotes caducados
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaCaducidad IS NOT NULL AND " +
           "l.fechaCaducidad < :fechaActual AND l.cantidad > 0")
    List<LoteInventario> findLotesCaducados(@Param("fechaActual") LocalDate fechaActual);
    
    // Lotes por rango de fechas de ingreso
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaIngreso BETWEEN :fechaInicio AND :fechaFin")
    List<LoteInventario> findLotesPorRangoFechaIngreso(@Param("fechaInicio") LocalDate fechaInicio, 
                                                      @Param("fechaFin") LocalDate fechaFin);
    
    // Obtener stock total por producto
    @Query("SELECT l.producto.idProducto, SUM(l.cantidad) FROM LoteInventario l " +
           "WHERE l.cantidad > 0 GROUP BY l.producto.idProducto")
    List<Object[]> getStockTotalPorProducto();
    
    // Lotes con mayor rotación (más vendidos)
    @Query("SELECT l, COUNT(dv) as ventasCount FROM LoteInventario l " +
           "LEFT JOIN DetalleVenta dv ON l.idLote = dv.lote.idLote " +
           "GROUP BY l.idLote ORDER BY ventasCount DESC")
    List<Object[]> findLotesConMayorRotacion();
    
    // Valor total del inventario
    @Query("SELECT SUM(l.cantidad * l.precioCompra) FROM LoteInventario l WHERE l.cantidad > 0")
    java.math.BigDecimal getValorTotalInventario();
    
    // Lotes por producto ordenados por fecha de caducidad
    @Query("SELECT l FROM LoteInventario l WHERE l.producto.idProducto = :idProducto AND l.cantidad > 0 " +
           "ORDER BY l.fechaCaducidad ASC NULLS LAST")
    List<LoteInventario> findLotesPorProductoOrdenadosPorCaducidad(@Param("idProducto") Long idProducto);
    
    // Lotes sin movimiento (sin ventas)
    @Query("SELECT l FROM LoteInventario l WHERE l.idLote NOT IN " +
           "(SELECT DISTINCT dv.lote.idLote FROM DetalleVenta dv) AND l.cantidad > 0")
    List<LoteInventario> findLotesSinMovimiento();
    
    // Buscar lotes por rango de precios
    @Query("SELECT l FROM LoteInventario l WHERE l.precioVenta BETWEEN :precioMinimo AND :precioMaximo")
    List<LoteInventario> findLotesPorRangoPrecios(@Param("precioMinimo") java.math.BigDecimal precioMinimo, 
                                                 @Param("precioMaximo") java.math.BigDecimal precioMaximo);
}
