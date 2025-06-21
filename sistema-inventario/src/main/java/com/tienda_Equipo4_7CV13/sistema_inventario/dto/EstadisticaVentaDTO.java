package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EstadisticaVentaDTO {
    
    private LocalDate fecha;
    private Long cantidadVentas;
    private BigDecimal totalIngresos;
    private String periodo; // DIA, SEMANA, MES
    
    // Constructores
    public EstadisticaVentaDTO() {}
    
    public EstadisticaVentaDTO(LocalDate fecha, Long cantidadVentas, BigDecimal totalIngresos) {
        this.fecha = fecha;
        this.cantidadVentas = cantidadVentas;
        this.totalIngresos = totalIngresos;
    }
    
    // Getters y Setters
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public Long getCantidadVentas() { return cantidadVentas; }
    public void setCantidadVentas(Long cantidadVentas) { this.cantidadVentas = cantidadVentas; }
    
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }
    
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
}
