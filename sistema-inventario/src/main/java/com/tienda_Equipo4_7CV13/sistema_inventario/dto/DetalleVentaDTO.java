package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public class DetalleVentaDTO {
    
    private Long idDetalle;
    
    @NotNull(message = "La venta es obligatoria")
    private Long idVenta;
    
    @NotNull(message = "El lote es obligatorio")
    private Long idLote;
    
    private String nombreProducto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private BigDecimal precioUnitario;
    
    private BigDecimal subtotal;
    
    // Constructores
    public DetalleVentaDTO() {}
    
    public DetalleVentaDTO(Long idLote, Integer cantidad, BigDecimal precioUnitario) {
        this.idLote = idLote;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.calcularSubtotal();
    }
    
    // Getters y Setters
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    
    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }
    
    public Long getIdLote() { return idLote; }
    public void setIdLote(Long idLote) { this.idLote = idLote; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { 
        this.cantidad = cantidad;
        this.calcularSubtotal();
    }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { 
        this.precioUnitario = precioUnitario;
        this.calcularSubtotal();
    }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    // Método para calcular subtotal
    private void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}
