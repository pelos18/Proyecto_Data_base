package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Notificacion;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.BusinessException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class NotificacionService {
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    // Obtener todas las notificaciones
    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }
    
    // Obtener notificación por ID
    public Notificacion findById(Long id) {
        return notificacionRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Notificación con ID " + id + " no encontrada"));
    }
    
    // Crear nueva notificación
    public Notificacion save(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }
    
    // Marcar notificación como leída
    public Notificacion marcarComoLeida(Long id) {
        Notificacion notificacion = findById(id);
        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }
    
    // Eliminar notificación
    public void deleteById(Long id) {
        notificacionRepository.deleteById(id);
    }
    
    // Obtener notificaciones por usuario
    public List<Notificacion> findByUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuarioIdUsuario(idUsuario);
    }
    
    // Obtener notificaciones no leídas por usuario
    public List<Notificacion> findNotificacionesNoLeidasPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuarioIdUsuarioAndLeidaFalse(idUsuario);
    }
    
    // Obtener notificaciones recientes por usuario
    public List<Notificacion> findNotificacionesRecientesPorUsuario(Long idUsuario) {
        return notificacionRepository.findNotificacionesRecientesPorUsuario(idUsuario);
    }
    
    // Contar notificaciones no leídas por usuario
    public Long contarNotificacionesNoLeidasPorUsuario(Long idUsuario) {
        return notificacionRepository.countNotificacionesNoLeidasPorUsuario(idUsuario);
    }
    
    // Obtener notificaciones por tipo
    public List<Notificacion> findByTipo(String tipo) {
        return notificacionRepository.findByTipo(tipo);
    }
    
    // Crear notificación de stock bajo
    public Notificacion crearNotificacionStockBajo(Usuario usuario, Producto producto) {
        String mensaje = String.format("El producto '%s' tiene stock bajo. Stock actual: %d, Stock mínimo: %d",
            producto.getNombre(), producto.getStockActual(), producto.getStockMinimo());
        
        Notificacion notificacion = new Notificacion(usuario, producto, "STOCK_BAJO", mensaje);
        return notificacionRepository.save(notificacion);
    }
    
    // Crear notificación de producto próximo a caducar
    public Notificacion crearNotificacionProductoCaducidad(Usuario usuario, Producto producto, LocalDate fechaCaducidad) {
        String mensaje = String.format("El producto '%s' caduca el %s",
            producto.getNombre(), fechaCaducidad.toString());
        
        Notificacion notificacion = new Notificacion(usuario, producto, "PRODUCTO_CADUCIDAD", mensaje);
        return notificacionRepository.save(notificacion);
    }
    
    // Crear notificación de venta realizada
    public Notificacion crearNotificacionVenta(Usuario usuario, String mensaje) {
        Notificacion notificacion = new Notificacion(usuario, "VENTA", mensaje);
        return notificacionRepository.save(notificacion);
    }
    
    // Crear notificación general
    public Notificacion crearNotificacion(Usuario usuario, String tipo, String mensaje) {
        Notificacion notificacion = new Notificacion(usuario, tipo, mensaje);
        return notificacionRepository.save(notificacion);
    }
    
    // Crear notificación con producto
    public Notificacion crearNotificacion(Usuario usuario, Producto producto, String tipo, String mensaje) {
        Notificacion notificacion = new Notificacion(usuario, producto, tipo, mensaje);
        return notificacionRepository.save(notificacion);
    }
    
    // Marcar todas las notificaciones como leídas para un usuario
    public void marcarTodasComoLeidasPorUsuario(Long idUsuario) {
        notificacionRepository.marcarTodasComoLeidasPorUsuario(idUsuario);
    }
    
    // Obtener notificaciones de stock bajo
    public List<Notificacion> getNotificacionesStockBajo() {
        return notificacionRepository.findNotificacionesStockBajo();
    }
    
    // Obtener notificaciones de productos próximos a caducar
    public List<Notificacion> getNotificacionesProductosCaducidad() {
        return notificacionRepository.findNotificacionesProductosCaducidad();
    }
    
    // Obtener estadísticas de notificaciones por tipo
    public List<Object[]> getEstadisticasNotificacionesPorTipo() {
        return notificacionRepository.getEstadisticasNotificacionesPorTipo();
    }
    
    // Limpiar notificaciones antiguas
    public void limpiarNotificacionesAntiguas(int diasAntiguedad) {
        LocalDate fechaLimite = LocalDate.now().minusDays(diasAntiguedad);
        notificacionRepository.eliminarNotificacionesAntiguas(fechaLimite);
    }
    
    // Obtener notificaciones por rango de fechas
    public List<Notificacion> findNotificacionesPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return notificacionRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }
}
