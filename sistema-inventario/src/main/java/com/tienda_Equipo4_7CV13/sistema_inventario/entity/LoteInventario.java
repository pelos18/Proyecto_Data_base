package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "LOTES_INVENTARIO")
@SequenceGenerator(name = "seq_lotes", sequenceName = "SEQ_LOTES_INVENTARIO", allocationSize = 1)
public class LoteInventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lotes")
    @Column(name = "ID_LOTE")
    private Long idLote;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROVEEDOR", nullable = false)
    private Proveedor proveedor;
    
    @NotNull
    @Min(0)
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;
    
    @NotNull
    @Column(name = "PRECIO_COMPRA", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioCompra;
    
    @NotNull
    @Column(name = "PRECIO_VENTA", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioVenta;
    
    @Column(name = "FECHA_INGRESO", nullable = false)
    private LocalDate fechaIngreso = LocalDate.now();
    
    @Column(name = "FECHA_CADUCIDAD")
    private LocalDate fechaCaducidad;
    
    // Constructores
    public LoteInventario() {}
    
    public LoteInventario(Producto producto, Proveedor proveedor, Integer cantidad, 
                         BigDecimal precioCompra, BigDecimal precioVenta) {
        this.producto = producto;
        this.proveedor = proveedor;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.fechaIngreso = LocalDate.now();
    }
    
    // Getters y Setters
    public Long getIdLote() { return idLote; }
    public void setIdLote(Long idLote) { this.idLote = idLote; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }
    
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}
