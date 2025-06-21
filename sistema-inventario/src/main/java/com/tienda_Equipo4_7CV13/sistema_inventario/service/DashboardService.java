package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private LoteInventarioService loteInventarioService;
    
    @Autowired
    private NotificacionService notificacionService;
    
    // Obtener total de productos activos
    public Long getTotalProductos() {
        return (long) productoService.findProductosActivos().size();
    }
    
    // Obtener total de clientes
    public Long getTotalClientes() {
        return (long) clienteService.findAll().size();
    }
    
    // Obtener total de ventas del día
    public Long getTotalVentasHoy() {
        return ventaService.contarVentasDelDia();
    }
    
    // Obtener ingresos del día
    public BigDecimal getIngresosDiarios() {
        BigDecimal ingresos = ventaService.getIngresosDelDia();
        return ingresos != null ? ingresos : BigDecimal.ZERO;
    }
    
    // Obtener ingresos del mes
    public BigDecimal getIngresosMensuales() {
        BigDecimal ingresos = ventaService.getIngresosDelMes();
        return ingresos != null ? ingresos : BigDecimal.ZERO;
    }
    
    // Obtener productos con stock bajo
    public List<Producto> getProductosStockBajo() {
        return productoService.findProductosStockBajo();
    }
    
    // Obtener ventas recientes
    public List<Venta> getVentasRecientes() {
        return ventaService.getVentasRecientes();
    }
    
    // Obtener productos más vendidos
    public List<Object[]> getProductosMasVendidos() {
        return productoService.findProductosMasVendidos();
    }
    
    // Obtener estadísticas de ventas por día (última semana)
    public List<Object[]> getEstadisticasVentasUltimaSemana() {
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusDays(7);
        return ventaService.getEstadisticasVentasPorDia(fechaInicio, fechaFin);
    }
    
    // Obtener estadísticas de ventas por usuario
    public List<Object[]> getEstadisticasVentasPorUsuario() {
        return ventaService.getEstadisticasVentasPorUsuario();
    }
    
    // Obtener valor total del inventario
    public BigDecimal getValorTotalInventario() {
        return loteInventarioService.getValorTotalInventario();
    }
    
    // Obtener productos sin stock
    public List<Producto> getProductosSinStock() {
        return productoService.findProductosSinStock();
    }
    
    // Obtener notificaciones no leídas para un usuario
    public List<com.tienda_Equipo4_7CV13.sistema_inventario.entity.Notificacion> getNotificacionesNoLeidas(Long idUsuario) {
        return notificacionService.findNotificacionesNoLeidasPorUsuario(idUsuario);
    }
    
    // Contar notificaciones no leídas para un usuario
    public Long contarNotificacionesNoLeidas(Long idUsuario) {
        return notificacionService.contarNotificacionesNoLeidasPorUsuario(idUsuario);
    }
    
    // Obtener resumen del dashboard
    public DashboardResumen getResumenDashboard(Long idUsuario) {
        DashboardResumen resumen = new DashboardResumen();
        
        resumen.setTotalProductos(getTotalProductos());
        resumen.setTotalClientes(getTotalClientes());
        resumen.setVentasHoy(getTotalVentasHoy());
        resumen.setIngresosDiarios(getIngresosDiarios());
        resumen.setIngresosMensuales(getIngresosMensuales());
        resumen.setProductosStockBajo(getProductosStockBajo().size());
        resumen.setProductosSinStock(getProductosSinStock().size());
        resumen.setValorInventario(getValorTotalInventario());
        resumen.setNotificacionesNoLeidas(contarNotificacionesNoLeidas(idUsuario));
        
        return resumen;
    }
    
    // Clase interna para el resumen del dashboard
    public static class DashboardResumen {
        private Long totalProductos;
        private Long totalClientes;
        private Long ventasHoy;
        private BigDecimal ingresosDiarios;
        private BigDecimal ingresosMensuales;
        private Integer productosStockBajo;
        private Integer productosSinStock;
        private BigDecimal valorInventario;
        private Long notificacionesNoLeidas;
        
        // Getters y Setters
        public Long getTotalProductos() { return totalProductos; }
        public void setTotalProductos(Long totalProductos) { this.totalProductos = totalProductos; }
        
        public Long getTotalClientes() { return totalClientes; }
        public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }
        
        public Long getVentasHoy() { return ventasHoy; }
        public void setVentasHoy(Long ventasHoy) { this.ventasHoy = ventasHoy; }
        
        public BigDecimal getIngresosDiarios() { return ingresosDiarios; }
        public void setIngresosDiarios(BigDecimal ingresosDiarios) { this.ingresosDiarios = ingresosDiarios; }
        
        public BigDecimal getIngresosMensuales() { return ingresosMensuales; }
        public void setIngresosMensuales(BigDecimal ingresosMensuales) { this.ingresosMensuales = ingresosMensuales; }
        
        public Integer getProductosStockBajo() { return productosStockBajo; }
        public void setProductosStockBajo(Integer productosStockBajo) { this.productosStockBajo = productosStockBajo; }
        
        public Integer getProductosSinStock() { return productosSinStock; }
        public void setProductosSinStock(Integer productosSinStock) { this.productosSinStock = productosSinStock; }
        
        public BigDecimal getValorInventario() { return valorInventario; }
        public void setValorInventario(BigDecimal valorInventario) { this.valorInventario = valorInventario; }
        
        public Long getNotificacionesNoLeidas() { return notificacionesNoLeidas; }
        public void setNotificacionesNoLeidas(Long notificacionesNoLeidas) { this.notificacionesNoLeidas = notificacionesNoLeidas; }
    }
}
