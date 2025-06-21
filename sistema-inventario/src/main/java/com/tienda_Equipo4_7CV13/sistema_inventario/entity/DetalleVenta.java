package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_VENTAS")
@SequenceGenerator(name = "seq_detalle_ventas", sequenceName = "SEQ_DETALLE_VENTAS", allocationSize = 1)
public class DetalleVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_detalle_ventas")
    @Column(name = "ID_DETALLE")
    private Long idDetalle;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENTA", nullable = false)
    private Venta venta;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOTE", nullable = false)
    private LoteInventario lote;
    
    @NotNull
    @Min(1)
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;
    
    @NotNull
    @Column(name = "PRECIO_UNITARIO", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    // Constructores
    public DetalleVenta() {}
    
    public DetalleVenta(Venta venta, LoteInventario lote, Integer cantidad, BigDecimal precioUnitario) {
        this.venta = venta;
        this.lote = lote;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    
    public LoteInventario getLote() { return lote; }
    public void setLote(LoteInventario lote) { this.lote = lote; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
    // Método para calcular subtotal
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
