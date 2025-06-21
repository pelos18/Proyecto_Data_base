package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {
    
    private Long totalProductos;
    private Long totalClientes;
    private Long ventasHoy;
    private BigDecimal ingresosHoy;
    private BigDecimal ingresosMes;
    
    private List<ProductoDTO> productosStockBajo;
    private List<VentaDTO> ventasRecientes;
    private List<EstadisticaVentaDTO> ventasPorDia;
    
    // Constructores
    public DashboardDTO() {}
    
    // Getters y Setters
    public Long getTotalProductos() { return totalProductos; }
    public void setTotalProductos(Long totalProductos) { this.totalProductos = totalProductos; }
    
    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }
    
    public Long getVentasHoy() { return ventasHoy; }
    public void setVentasHoy(Long ventasHoy) { this.ventasHoy = ventasHoy; }
    
    public BigDecimal getIngresosHoy() { return ingresosHoy; }
    public void setIngresosHoy(BigDecimal ingresosHoy) { this.ingresosHoy = ingresosHoy; }
    
    public BigDecimal getIngresosMes() { return ingresosMes; }
    public void setIngresosMes(BigDecimal ingresosMes) { this.ingresosMes = ingresosMes; }
    
    public List<ProductoDTO> getProductosStockBajo() { return productosStockBajo; }
    public void setProductosStockBajo(List<ProductoDTO> productosStockBajo) { this.productosStockBajo = productosStockBajo; }
    
    public List<VentaDTO> getVentasRecientes() { return ventasRecientes; }
    public void setVentasRecientes(List<VentaDTO> ventasRecientes) { this.ventasRecientes = ventasRecientes; }
    
    public List<EstadisticaVentaDTO> getVentasPorDia() { return ventasPorDia; }
    public void setVentasPorDia(List<EstadisticaVentaDTO> ventasPorDia) { this.ventasPorDia = ventasPorDia; }
}
