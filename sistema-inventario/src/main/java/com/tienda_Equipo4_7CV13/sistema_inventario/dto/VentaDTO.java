package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class VentaDTO {
    
    private Long idVenta;
    
    private Long idCliente;
    private String nombreCliente;
    
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    private String nombreUsuario;
    
    private LocalDateTime fechaVenta;
    
    private BigDecimal total;
    
    private String estado;
    
    @Valid
    private List<DetalleVentaDTO> detalles = new ArrayList<>();
    
    // Constructores
    public VentaDTO() {
        this.fechaVenta = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.total = BigDecimal.ZERO;
    }
    
    public VentaDTO(Long idUsuario, Long idCliente) {
        this();
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
    }
    
    // Getters y Setters
    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }
    
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<DetalleVentaDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaDTO> detalles) { this.detalles = detalles; }
    
    // Método para calcular el total
    public void calcularTotal() {
        this.total = detalles.stream()
            .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
