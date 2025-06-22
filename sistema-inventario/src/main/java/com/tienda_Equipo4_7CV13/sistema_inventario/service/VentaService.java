package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public Venta findById(Long id) {
        return ventaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }

    public List<Venta> findByEstado(String estado) {
        return ventaRepository.findByEstado(estado);
    }

    public List<Venta> findByClienteId(Long clienteId) {
        return ventaRepository.findByClienteIdCliente(clienteId);
    }

    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioIdUsuario(usuarioId);
    }

    public List<Venta> findByFecha(LocalDate fecha) {
        return ventaRepository.findByFechaVenta(fecha);
    }

    public List<Venta> findVentasByFecha(LocalDate fecha) {
        return ventaRepository.findByFechaVenta(fecha);
    }

    public List<Venta> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }

    public List<Venta> findVentasByRangoFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }

    public BigDecimal getVentasDelDia() {
        return ventaRepository.findVentasDelDia();
    }

    public BigDecimal getVentasDelMes() {
        return ventaRepository.findVentasDelMes();
    }

    public Long countVentasCompletadas() {
        return ventaRepository.countVentasCompletadas();
    }

    public BigDecimal getIngresosDelDia() {
        return ventaRepository.getIngresosDelDia();
    }

    public BigDecimal getIngresosDelMes() {
        return ventaRepository.getIngresosDelMes();
    }

    public Long countVentasDelDia() {
        return ventaRepository.countVentasDelDia();
    }

    public List<Venta> getVentasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findVentasByFechaBetween(fechaInicio, fechaFin);
    }

    public List<Venta> getTotalVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.getTotalVentasPorDia(fechaInicio, fechaFin);
    }

    public List<Venta> getEstadisticasVentasPorUsuario() {
        return ventaRepository.getEstadisticasVentasPorUsuario();
    }

    @Transactional
    public Venta confirmarVenta(Long id) {
        Venta venta = findById(id);
        venta.setEstado("COMPLETADA");
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta cancelarVenta(Long id) {
        Venta venta = findById(id);
        venta.setEstado("CANCELADA");
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta procesarVenta(Venta venta) {
        // Guardar la venta
        Venta ventaGuardada = ventaRepository.save(venta);
        
        // Procesar cada detalle y actualizar stock
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                // Verificar stock disponible
                Producto producto = detalle.getProducto();
                if (producto.getStockActual() < detalle.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
                }
                
                // Actualizar stock del producto
                producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
                productoRepository.save(producto);
                
                // Guardar detalle
                detalle.setVenta(ventaGuardada);
                detalleVentaRepository.save(detalle);
            }
        }
        
        return ventaGuardada;
    }
}
