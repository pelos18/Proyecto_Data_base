package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    List<Venta> findByEstado(String estado);
    
    List<Venta> findByClienteIdCliente(Long idCliente);
    
    List<Venta> findByUsuarioIdUsuario(Long idUsuario);
    
    @Query("SELECT v FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) = :fecha")
    List<Venta> findByFechaVenta(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT v FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findByFechaVentaBetween(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
    List<Venta> findVentasByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) = CURRENT_DATE AND v.estado = 'COMPLETADA'")
    BigDecimal findVentasDelDia();
    
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE FUNCTION('MONTH', v.fechaVenta) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', v.fechaVenta) = FUNCTION('YEAR', CURRENT_DATE) AND v.estado = 'COMPLETADA'")
    BigDecimal findVentasDelMes();
    
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.estado = 'COMPLETADA'")
    Long countVentasCompletadas();
    
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) = CURRENT_DATE AND v.estado IN ('COMPLETADA', 'ACEPTADA')")
    BigDecimal getIngresosDelDia();
    
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE FUNCTION('MONTH', v.fechaVenta) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', v.fechaVenta) = FUNCTION('YEAR', CURRENT_DATE) AND v.estado IN ('COMPLETADA', 'ACEPTADA')")
    BigDecimal getIngresosDelMes();
    
    @Query("SELECT COUNT(v) FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) = CURRENT_DATE AND v.estado IN ('COMPLETADA', 'ACEPTADA')")
    Long countVentasDelDia();
    
    @Query("SELECT v FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) BETWEEN :fechaInicio AND :fechaFin AND v.estado IN ('COMPLETADA', 'ACEPTADA')")
    List<Venta> getTotalVentasPorDia(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT v FROM Venta v WHERE v.estado IN ('COMPLETADA', 'ACEPTADA') GROUP BY v.usuario")
    List<Venta> getEstadisticasVentasPorUsuario();
}
