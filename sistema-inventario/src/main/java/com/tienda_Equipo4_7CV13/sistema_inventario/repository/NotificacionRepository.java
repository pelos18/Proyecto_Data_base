package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    // Buscar notificaciones por usuario
    List<Notificacion> findByUsuarioIdUsuario(Long idUsuario);
    
    // Buscar notificaciones no leídas por usuario
    List<Notificacion> findByUsuarioIdUsuarioAndLeidaFalse(Long idUsuario);
    
    // Buscar notificaciones por tipo
    List<Notificacion> findByTipo(String tipo);
    
    // Buscar notificaciones por producto
    List<Notificacion> findByProductoIdProducto(Long idProducto);
    
    // Contar notificaciones no leídas por usuario
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario AND n.leida = false")
    Long countNotificacionesNoLeidasPorUsuario(@Param("idUsuario") Long idUsuario);
    
    // Notificaciones recientes (últimas 10)
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "ORDER BY n.fechaCreacion DESC, n.idNotificacion DESC LIMIT 10")
    List<Notificacion> findNotificacionesRecientesPorUsuario(@Param("idUsuario") Long idUsuario);
    
    // Notificaciones por fecha
    List<Notificacion> findByFechaCreacion(LocalDate fecha);
    
    // Notificaciones por rango de fechas
    List<Notificacion> findByFechaCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Notificaciones por tipo y usuario
    List<Notificacion> findByTipoAndUsuarioIdUsuario(String tipo, Long idUsuario);
    
    // Notificaciones no leídas por tipo
    @Query("SELECT n FROM Notificacion n WHERE n.tipo = :tipo AND n.leida = false ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findNotificacionesNoLeidasPorTipo(@Param("tipo") String tipo);
    
    // Marcar todas las notificaciones como leídas para un usuario
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.usuario.idUsuario = :idUsuario AND n.leida = false")
    void marcarTodasComoLeidasPorUsuario(@Param("idUsuario") Long idUsuario);
    
    // Eliminar notificaciones antiguas (más de X días)
    @Query("DELETE FROM Notificacion n WHERE n.fechaCreacion < :fechaLimite")
    void eliminarNotificacionesAntiguas(@Param("fechaLimite") LocalDate fechaLimite);
    
    // Estadísticas de notificaciones por tipo
    @Query("SELECT n.tipo, COUNT(n) FROM Notificacion n GROUP BY n.tipo ORDER BY COUNT(n) DESC")
    List<Object[]> getEstadisticasNotificacionesPorTipo();
    
    // Notificaciones de stock bajo
    @Query("SELECT n FROM Notificacion n WHERE n.tipo = 'STOCK_BAJO' AND n.leida = false " +
           "ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findNotificacionesStockBajo();
    
    // Notificaciones de productos próximos a caducar
    @Query("SELECT n FROM Notificacion n WHERE n.tipo = 'PRODUCTO_CADUCIDAD' AND n.leida = false " +
           "ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findNotificacionesProductosCaducidad();
}
