package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@SequenceGenerator(name = "seq_ventas", sequenceName = "SEQ_VENTAS", allocationSize = 1)
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ventas")
    @Column(name = "ID_VENTA")
    private Long idVenta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;
    
    @Column(name = "FECHA_VENTA", nullable = false)
    private LocalDate fechaVenta = LocalDate.now();
    
    @Column(name = "TOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;
    
    @Column(name = "ESTADO", nullable = false)
    private String estado = "PENDIENTE";
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles = new ArrayList<>();
    
    // Constructores
    public Venta() {}
    
    public Venta(Usuario usuario, Cliente cliente) {
        this.usuario = usuario;
        this.cliente = cliente;
        this.fechaVenta = LocalDate.now();
        this.estado = "PENDIENTE";
    }
    
    // Getters y Setters
    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}
