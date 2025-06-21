package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    
    // Buscar caja por usuario y fecha
    Optional<Caja> findByUsuarioIdUsuarioAndFecha(Long idUsuario, LocalDate fecha);
    
    // Buscar cajas por usuario
    List<Caja> findByUsuarioIdUsuario(Long idUsuario);
    
    // Buscar cajas por fecha
    List<Caja> findByFecha(LocalDate fecha);
    
    // Buscar cajas por rango de fechas
    List<Caja> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Caja del día actual
    @Query("SELECT c FROM Caja c WHERE c.fecha = CURRENT_DATE")
    List<Caja> findCajasDelDia();
    
    // Caja del día por usuario
    @Query("SELECT c FROM Caja c WHERE c.usuario.idUsuario = :idUsuario AND c.fecha = CURRENT_DATE")
    Optional<Caja> findCajaDelDiaPorUsuario(@Param("idUsuario") Long idUsuario);
    
    // Total de ingresos por día
    @Query("SELECT c.fecha, SUM(c.ingresos) FROM Caja c " +
           "WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY c.fecha ORDER BY c.fecha")
    List<Object[]> getTotalIngresosPorDia(@Param("fechaInicio") LocalDate fechaInicio, 
                                         @Param("fechaFin") LocalDate fechaFin);
    
    // Total de egresos por día
    @Query("SELECT c.fecha, SUM(c.egresos) FROM Caja c " +
           "WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY c.fecha ORDER BY c.fecha")
    List<Object[]> getTotalEgresosPorDia(@Param("fechaInicio") LocalDate fechaInicio, 
                                        @Param("fechaFin") LocalDate fechaFin);
    
    // Saldo final del día
    @Query("SELECT COALESCE(SUM(c.saldoFinal), 0) FROM Caja c WHERE c.fecha = :fecha")
    BigDecimal getSaldoFinalDelDia(@Param("fecha") LocalDate fecha);
    
    // Ingresos del mes
    @Query("SELECT COALESCE(SUM(c.ingresos), 0) FROM Caja c WHERE " +
           "EXTRACT(MONTH FROM c.fecha) = EXTRACT(MONTH FROM :fecha) " +
           "AND EXTRACT(YEAR FROM c.fecha) = EXTRACT(YEAR FROM :fecha)")
    BigDecimal getIngresosDelMes(@Param("fecha") LocalDate fecha);
    
    // Egresos del mes
    @Query("SELECT COALESCE(SUM(c.egresos), 0) FROM Caja c WHERE " +
           "EXTRACT(MONTH FROM c.fecha) = EXTRACT(MONTH FROM :fecha) " +
           "AND EXTRACT(YEAR FROM c.fecha) = EXTRACT(YEAR FROM :fecha)")
    BigDecimal getEgresosDelMes(@Param("fecha") LocalDate fecha);
    
    // Estadísticas de caja por usuario
    @Query("SELECT u.nombre, COUNT(c), COALESCE(SUM(c.ingresos), 0), COALESCE(SUM(c.egresos), 0) " +
           "FROM Usuario u LEFT JOIN Caja c ON u.idUsuario = c.usuario.idUsuario " +
           "WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin OR c.fecha IS NULL " +
           "GROUP BY u.idUsuario, u.nombre ORDER BY COALESCE(SUM(c.ingresos), 0) DESC")
    List<Object[]> getEstadisticasCajaPorUsuario(@Param("fechaInicio") LocalDate fechaInicio, 
                                                @Param("fechaFin") LocalDate fechaFin);
    
    // Últimas cajas registradas
    @Query("SELECT c FROM Caja c ORDER BY c.fecha DESC, c.idCaja DESC LIMIT 10")
    List<Caja> findUltimasCajas();
    
    // Promedio de ingresos diarios
    @Query("SELECT AVG(c.ingresos) FROM Caja c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal getPromedioIngresosDiarios(@Param("fechaInicio") LocalDate fechaInicio, 
                                         @Param("fechaFin") LocalDate fechaFin);
}
