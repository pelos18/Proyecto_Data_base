package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PRODUCTOS")
@SequenceGenerator(name = "seq_productos", sequenceName = "SEQ_PRODUCTOS", allocationSize = 1)
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_productos")
    @Column(name = "ID_PRODUCTO")
    private Long idProducto;
    
    @Size(max = 100)
    @Column(name = "CODIGO_BARRAS", unique = true)
    private String codigoBarras;
    
    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;
    
    @Size(max = 255)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA", nullable = false)
    private Categoria categoria;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MARCA", nullable = false)
    private Marca marca;
    
    @Column(name = "STOCK_MINIMO", nullable = false)
    private Integer stockMinimo = 0;
    
    @Column(name = "STOCK_ACTUAL", nullable = false)
    private Integer stockActual = 0;
    
    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;
    
    // Constructores
    public Producto() {}
    
    public Producto(String nombre, String descripcion, Categoria categoria, Marca marca) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.marca = marca;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    
    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }
    
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
