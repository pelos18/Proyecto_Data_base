package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
   
   // Buscar ventas por cliente
   List<Venta> findByClienteIdCliente(Long idCliente);
   
   // Buscar ventas por usuario
   List<Venta> findByUsuarioIdUsuario(Long idUsuario);
   
   // Buscar ventas por estado
   List<Venta> findByEstado(String estado);
   
   // Buscar ventas por fecha
   List<Venta> findByFechaVenta(LocalDate fecha);
   
   // Buscar ventas por rango de fechas
   List<Venta> findByFechaVentaBetween(LocalDate fechaInicio, LocalDate fechaFin);
   
   // Ventas del día actual
   @Query("SELECT v FROM Venta v WHERE v.fechaVenta = CURRENT_DATE")
   List<Venta> findVentasDelDia();
   
   // Ventas de la semana actual
   @Query("SELECT v FROM Venta v WHERE v.fechaVenta >= :fechaInicio AND v.fechaVenta <= :fechaFin")
   List<Venta> findVentasDeLaSemana(@Param("fechaInicio") LocalDate fechaInicio, 
                                   @Param("fechaFin") LocalDate fechaFin);
   
   // Ventas del mes actual
   @Query("SELECT v FROM Venta v WHERE EXTRACT(MONTH FROM v.fechaVenta) = EXTRACT(MONTH FROM CURRENT_DATE) " +
          "AND EXTRACT(YEAR FROM v.fechaVenta) = EXTRACT(YEAR FROM CURRENT_DATE)")
   List<Venta> findVentasDelMes();
   
   // Total de ventas por día
   @Query("SELECT v.fechaVenta, COUNT(v), SUM(v.total) FROM Venta v " +
          "WHERE v.estado = 'ACEPTADA' AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
          "GROUP BY v.fechaVenta ORDER BY v.fechaVenta")
   List<Object[]> getTotalVentasPorDia(@Param("fechaInicio") LocalDate fechaInicio, 
                                      @Param("fechaFin") LocalDate fechaFin);
   
   // Ingresos del día
   @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta = CURRENT_DATE AND v.estado = 'ACEPTADA'")
   BigDecimal getIngresosDelDia();
   
   // Ingresos del mes
   @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE " +
          "EXTRACT(MONTH FROM v.fechaVenta) = EXTRACT(MONTH FROM CURRENT_DATE) " +
          "AND EXTRACT(YEAR FROM v.fechaVenta) = EXTRACT(YEAR FROM CURRENT_DATE) " +
          "AND v.estado = 'ACEPTADA'")
   BigDecimal getIngresosDelMes();
   
   // Contar ventas del día
   @Query("SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta = CURRENT_DATE")
   Long countVentasDelDia();
   
   // Ventas por cliente en un período
   @Query("SELECT v FROM Venta v WHERE v.cliente.idCliente = :idCliente " +
          "AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
   List<Venta> findVentasPorClienteEnPeriodo(@Param("idCliente") Long idCliente, 
                                            @Param("fechaInicio") LocalDate fechaInicio, 
                                            @Param("fechaFin") LocalDate fechaFin);
   
   // Ventas por usuario en un período
   @Query("SELECT v FROM Venta v WHERE v.usuario.idUsuario = :idUsuario " +
          "AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
   List<Venta> findVentasPorUsuarioEnPeriodo(@Param("idUsuario") Long idUsuario, 
                                            @Param("fechaInicio") LocalDate fechaInicio, 
                                            @Param("fechaFin") LocalDate fechaFin);
   
   // Ventas por rango de montos
   @Query("SELECT v FROM Venta v WHERE v.total BETWEEN :montoMinimo AND :montoMaximo " +
          "AND v.estado = 'ACEPTADA' ORDER BY v.total DESC")
   List<Venta> findVentasPorRangoMontos(@Param("montoMinimo") BigDecimal montoMinimo, 
                                       @Param("montoMaximo") BigDecimal montoMaximo);
   
   // Estadísticas de ventas por usuario
   @Query("SELECT u.nombre, COUNT(v), COALESCE(SUM(v.total), 0) FROM Usuario u " +
          "LEFT JOIN Venta v ON u.idUsuario = v.usuario.idUsuario AND v.estado = 'ACEPTADA' " +
          "GROUP BY u.idUsuario, u.nombre ORDER BY COALESCE(SUM(v.total), 0) DESC")
   List<Object[]> getEstadisticasVentasPorUsuario();
}
