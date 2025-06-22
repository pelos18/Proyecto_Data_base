package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteInventarioRepository extends JpaRepository<LoteInventario, Long> {
    
    List<LoteInventario> findByProductoIdProducto(Long productoId);
    
    List<LoteInventario> findByProveedorIdProveedor(Long idProveedor);
    
    List<LoteInventario> findByCantidadGreaterThan(int cantidad);
    
    List<LoteInventario> findByProductoIdProductoAndCantidadGreaterThan(Long idProducto, int cantidad);
    
    @Query("SELECT l.precioVenta FROM LoteInventario l WHERE l.producto.idProducto = :productoId AND l.cantidad > 0 ORDER BY l.fechaIngreso DESC")
    List<Double> findPrecioVentaByProductoIdList(@Param("productoId") Long productoId);
    
    default Double findPrecioVentaByProductoId(Long productoId) {
        List<Double> precios = findPrecioVentaByProductoIdList(productoId);
        return precios.isEmpty() ? null : precios.get(0);
    }
    
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaCaducidad BETWEEN :fechaActual AND :fechaLimite AND l.cantidad > 0")
    List<LoteInventario> findLotesProximosACaducar(@Param("fechaActual") LocalDate fechaActual, @Param("fechaLimite") LocalDate fechaLimite);
    
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaCaducidad <= :fecha AND l.cantidad > 0")
    List<LoteInventario> findLotesCaducados(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaCaducidad <= :fecha AND l.cantidad > 0")
    List<LoteInventario> findLotesProximosAVencer(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT COUNT(l) FROM LoteInventario l WHERE l.cantidad > 0")
    Long countLotesActivos();
    
    @Query("SELECT COUNT(l) FROM LoteInventario l WHERE l.fechaCaducidad <= :fecha AND l.cantidad > 0")
    Long countLotesProximosAVencer(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT l FROM LoteInventario l GROUP BY l.producto")
    List<LoteInventario> getStockTotalPorProducto();
    
    @Query("SELECT COALESCE(SUM(l.cantidad * l.precioCompra), 0) FROM LoteInventario l WHERE l.cantidad > 0")
    BigDecimal getValorTotalInventario();
    
    @Query("SELECT l FROM LoteInventario l WHERE l.producto.idProducto = :idProducto ORDER BY l.fechaCaducidad ASC")
    List<LoteInventario> findLotesPorProductoOrdenadosPorCaducidad(@Param("idProducto") Long idProducto);
    
    @Query("SELECT l FROM LoteInventario l WHERE l.cantidad > 0")
    List<LoteInventario> findLotesSinMovimiento();
    
    @Query("SELECT l FROM LoteInventario l WHERE l.cantidad > 0 ORDER BY l.cantidad DESC")
    List<LoteInventario> findLotesConMayorRotacion();
    
    @Query("SELECT l FROM LoteInventario l WHERE l.fechaIngreso BETWEEN :fechaInicio AND :fechaFin")
    List<LoteInventario> findLotesPorRangoFechaIngreso(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT l FROM LoteInventario l WHERE l.precioVenta BETWEEN :precioMinimo AND :precioMaximo")
    List<LoteInventario> findLotesPorRangoPrecios(@Param("precioMinimo") BigDecimal precioMinimo, @Param("precioMaximo") BigDecimal precioMaximo);
}
