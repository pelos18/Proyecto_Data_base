package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
    // Buscar detalles por venta
    List<DetalleVenta> findByVentaIdVenta(Long idVenta);
    
    // Buscar detalles por lote
    List<DetalleVenta> findByLoteIdLote(Long idLote);
    
    // Productos más vendidos
    @Query("SELECT l.producto.nombre, SUM(dv.cantidad), SUM(dv.cantidad * dv.precioUnitario) " +
           "FROM DetalleVenta dv JOIN dv.lote l JOIN dv.venta v " +
           "WHERE v.estado = 'ACEPTADA' " +
           "GROUP BY l.producto.idProducto, l.producto.nombre " +
           "ORDER BY SUM(dv.cantidad) DESC")
    List<Object[]> findProductosMasVendidos();
    
    // Productos más vendidos en un período
    @Query("SELECT l.producto.nombre, SUM(dv.cantidad), SUM(dv.cantidad * dv.precioUnitario) " +
           "FROM DetalleVenta dv JOIN dv.lote l JOIN dv.venta v " +
           "WHERE v.estado = 'ACEPTADA' AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY l.producto.idProducto, l.producto.nombre " +
           "ORDER BY SUM(dv.cantidad) DESC")
    List<Object[]> findProductosMasVendidosEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, 
                                                    @Param("fechaFin") LocalDate fechaFin);
    
    // Ventas por producto
    @Query("SELECT dv FROM DetalleVenta dv JOIN dv.lote l " +
           "WHERE l.producto.idProducto = :idProducto")
    List<DetalleVenta> findVentasPorProducto(@Param("idProducto") Long idProducto);
    
    // Total vendido por producto
    @Query("SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv JOIN dv.lote l JOIN dv.venta v " +
           "WHERE l.producto.idProducto = :idProducto AND v.estado = 'ACEPTADA'")
    Long getTotalVendidoPorProducto(@Param("idProducto") Long idProducto);
    
    // Ingresos por producto
    @Query("SELECT COALESCE(SUM(dv.cantidad * dv.precioUnitario), 0) FROM DetalleVenta dv " +
           "JOIN dv.lote l JOIN dv.venta v " +
           "WHERE l.producto.idProducto = :idProducto AND v.estado = 'ACEPTADA'")
    java.math.BigDecimal getIngresosPorProducto(@Param("idProducto") Long idProducto);
    
    // Detalles de ventas en un período
    @Query("SELECT dv FROM DetalleVenta dv JOIN dv.venta v " +
           "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<DetalleVenta> findDetallesEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, 
                                            @Param("fechaFin") LocalDate fechaFin);
    
    // Cantidad total vendida en un período
    @Query("SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv JOIN dv.venta v " +
           "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.estado = 'ACEPTADA'")
    Long getCantidadTotalVendidaEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, 
                                         @Param("fechaFin") LocalDate fechaFin);
    
    // Promedio de productos por venta
    @Query("SELECT AVG(subquery.cantidadProductos) FROM " +
           "(SELECT COUNT(dv) as cantidadProductos FROM DetalleVenta dv " +
           "JOIN dv.venta v WHERE v.estado = 'ACEPTADA' GROUP BY v.idVenta) subquery")
    Double getPromedioProductosPorVenta();
    
    // Lotes más vendidos
    @Query("SELECT l, SUM(dv.cantidad) as totalVendido FROM DetalleVenta dv " +
           "JOIN dv.lote l JOIN dv.venta v " +
           "WHERE v.estado = 'ACEPTADA' " +
           "GROUP BY l.idLote ORDER BY totalVendido DESC")
    List<Object[]> findLotesMasVendidos();
}
