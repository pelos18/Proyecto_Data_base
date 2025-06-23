package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTOS")
public class Producto {
    
    @Id
    @Column(name = "ID_PRODUCTO", nullable = false)
    private Long idProducto; // NO usar @GeneratedValue porque el trigger lo maneja

    @Column(name = "CODIGO_BARRAS", length = 100, nullable = true)
    private String codigoBarras;

    @Column(name = "NOMBRE", length = 150, nullable = false)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 255, nullable = true)
    private String descripcion;

    @Column(name = "ID_CATEGORIA", nullable = false)
    private Long idCategoria;

    @Column(name = "ID_MARCA", nullable = false)
    private Long idMarca;

    @Column(name = "STOCK_MINIMO", nullable = false)
    private Long stockMinimo = 0L; // Valor por defecto

    @Column(name = "STOCK_ACTUAL", nullable = false)
    private Long stockActual = 0L; // Valor por defecto

    @Column(name = "ACTIVO", nullable = false)
    private Long activo = 1L; // Valor por defecto (1 = activo, 0 = inactivo)

    // Relaciones opcionales para mostrar nombres (LAZY para evitar problemas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA", insertable = false, updatable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MARCA", insertable = false, updatable = false)
    private Marca marca;

    // Constructores
    public Producto() {
        this.stockMinimo = 0L;
        this.stockActual = 0L;
        this.activo = 1L;
    }

    public Producto(String codigoBarras, String nombre, String descripcion, 
                   Long idCategoria, Long idMarca, Long stockMinimo) {
        this();
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.stockMinimo = stockMinimo != null ? stockMinimo : 0L;
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

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }

    public Long getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Long stockMinimo) { 
        this.stockMinimo = stockMinimo != null ? stockMinimo : 0L; 
    }

    public Long getStockActual() { return stockActual; }
    public void setStockActual(Long stockActual) { 
        this.stockActual = stockActual != null ? stockActual : 0L; 
    }

    public Long getActivo() { return activo; }
    public void setActivo(Long activo) { 
        this.activo = activo != null ? activo : 1L; 
    }

    // Métodos de conveniencia para boolean
    public boolean isActivo() { return this.activo != null && this.activo == 1L; }
    public void setActivoBoolean(boolean activo) { this.activo = activo ? 1L : 0L; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", idCategoria=" + idCategoria +
                ", idMarca=" + idMarca +
                ", stockMinimo=" + stockMinimo +
                ", stockActual=" + stockActual +
                ", activo=" + activo +
                '}';
    }
}
