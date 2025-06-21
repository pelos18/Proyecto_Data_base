package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.DetalleVenta;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.VentaException;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.StockInsuficienteException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private LoteInventarioService loteInventarioService;
    
    @Autowired
    private ProductoService productoService;
    
    // Obtener todas las ventas
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }
    
    // Obtener venta por ID
    public Venta findById(Long id) {
        return ventaRepository.findById(id)
            .orElseThrow(() -> new VentaException.VentaNotFoundException(id));
    }
    
    // Crear nueva venta
    public Venta save(Venta venta) {
        // Validar que la venta tenga detalles
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new VentaException.VentaSinDetallesException();
        }
        
        // Calcular total
        BigDecimal total = calcularTotal(venta);
        venta.setTotal(total);
        
        return ventaRepository.save(venta);
    }
    
    // Actualizar venta
    public Venta update(Venta venta) {
        Venta ventaExistente = findById(venta.getIdVenta());
        
        // Solo se puede actualizar si está en estado PENDIENTE
        if (!"PENDIENTE".equals(ventaExistente.getEstado())) {
            throw new VentaException.EstadoVentaInvalidoException(ventaExistente.getEstado(), "PENDIENTE");
        }
        
        // Calcular total
        BigDecimal total = calcularTotal(venta);
        venta.setTotal(total);
        
        return ventaRepository.save(venta);
    }
    
    // Confirmar venta
    public Venta confirmarVenta(Long id) {
        Venta venta = findById(id);
        
        // Verificar que esté en estado PENDIENTE
        if (!"PENDIENTE".equals(venta.getEstado())) {
            throw new VentaException.VentaYaConfirmadaException(id);
        }
        
        // Verificar stock disponible para todos los productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getLote().getCantidad() < detalle.getCantidad()) {
                throw new StockInsuficienteException(
                    detalle.getLote().getProducto().getIdProducto(),
                    detalle.getLote().getProducto().getNombre(),
                    detalle.getLote().getCantidad(),
                    detalle.getCantidad()
                );
            }
        }
        
        // Reducir stock de los lotes
        for (DetalleVenta detalle : venta.getDetalles()) {
            loteInventarioService.reducirStock(detalle.getLote().getIdLote(), detalle.getCantidad());
            
            // Actualizar stock del producto
            productoService.reducirStock(
                detalle.getLote().getProducto().getIdProducto(), 
                detalle.getCantidad()
            );
        }
        
        // Cambiar estado a ACEPTADA
        venta.setEstado("ACEPTADA");
        
        return ventaRepository.save(venta);
    }
    
    // Cancelar venta
    public Venta cancelarVenta(Long id) {
        Venta venta = findById(id);
        
        // Solo se puede cancelar si está PENDIENTE
        if (!"PENDIENTE".equals(venta.getEstado())) {
            throw new VentaException.EstadoVentaInvalidoException(venta.getEstado(), "PENDIENTE");
        }
        
        venta.setEstado("CANCELADA");
        return ventaRepository.save(venta);
    }
    
    // Obtener ventas por cliente
    public List<Venta> findByCliente(Long idCliente) {
        return ventaRepository.findByClienteIdCliente(idCliente);
    }
    
    // Obtener ventas por usuario
    public List<Venta> findByUsuario(Long idUsuario) {
        return ventaRepository.findByUsuarioIdUsuario(idUsuario);
    }
    
    // Obtener ventas por estado
    public List<Venta> findByEstado(String estado) {
        return ventaRepository.findByEstado(estado);
    }
    
    // Obtener ventas por fecha
    public List<Venta> findVentasByFecha(LocalDate fecha) {
        return ventaRepository.findByFechaVenta(fecha);
    }
    
    // Obtener ventas por rango de fechas
    public List<Venta> findVentasByRangoFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }
    
    // Obtener ventas del día
    public List<Venta> getVentasDelDia() {
        return ventaRepository.findVentasDelDia();
    }
    
    // Obtener ventas recientes
    public List<Venta> getVentasRecientes() {
        return ventaRepository.findVentasRecientes();
    }
    
    // Obtener ingresos del día
    public BigDecimal getIngresosDelDia() {
        return ventaRepository.getIngresosDelDia();
    }
    
    // Obtener ingresos del mes
    public BigDecimal getIngresosDelMes() {
        return ventaRepository.getIngresosDelMes();
    }
    
    // Contar ventas del día
    public Long contarVentasDelDia() {
        return ventaRepository.countVentasDelDia();
    }
    
    // Obtener estadísticas de ventas por día
    public List<Object[]> getEstadisticasVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.getTotalVentasPorDia(fechaInicio, fechaFin);
    }
    
    // Obtener estadísticas de ventas por usuario
    public List<Object[]> getEstadisticasVentasPorUsuario() {
        return ventaRepository.getEstadisticasVentasPorUsuario();
    }
    
    // Calcular total de la venta
    private BigDecimal calcularTotal(Venta venta) {
        return venta.getDetalles().stream()
            .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Validar venta antes de guardar
    private void validarVenta(Venta venta) {
        if (venta.getUsuario() == null) {
            throw new VentaException("La venta debe tener un usuario asignado");
        }
        
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new VentaException.VentaSinDetallesException();
        }
        
        // Validar cada detalle
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getCantidad() <= 0) {
                throw new VentaException("La cantidad debe ser mayor a 0");
            }
            
            if (detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new VentaException("El precio unitario debe ser mayor a 0");
            }
        }
    }
}
