package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @Column(name = "id_marca", nullable = false)
    private Long idMarca;

    @Column(name = "stock_minimo")
    private Long stockMinimo;

    @Column(name = "stock_actual")
    private Long stockActual;

    @Column(name = "activo")
    private Long activo;

    // Relaciones JPA para obtener nombres (LAZY para evitar problemas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", insertable = false, updatable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", insertable = false, updatable = false)
    private Marca marca;

    // Constructor vacío
    public Producto() {
        this.activo = 1L;
        this.stockActual = 0L;
        this.stockMinimo = 0L;
    }

    // Constructor con parámetros básicos
    public Producto(String nombre, String descripcion, String codigoBarras, Long stockMinimo) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigoBarras = codigoBarras;
        this.stockMinimo = stockMinimo;
    }

    // Constructor completo
    public Producto(String nombre, String descripcion, String codigoBarras, 
                   Long idCategoria, Long idMarca, Long stockMinimo) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigoBarras = codigoBarras;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.stockMinimo = stockMinimo;
    }

    // Getters y Setters
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public Long getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Long stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Long getStockActual() {
        return stockActual;
    }

    public void setStockActual(Long stockActual) {
        this.stockActual = stockActual;
    }

    public Long getActivo() {
        return activo;
    }

    public void setActivo(Long activo) {
        this.activo = activo;
    }

    // Relaciones JPA
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        if (categoria != null) {
            this.idCategoria = categoria.getIdCategoria();
        }
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
        if (marca != null) {
            this.idMarca = marca.getIdMarca();
        }
    }

    // Métodos de utilidad
    public boolean isActivo() {
        return this.activo != null && this.activo == 1L;
    }

    public void activar() {
        this.activo = 1L;
    }

    public void desactivar() {
        this.activo = 0L;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", idCategoria=" + idCategoria +
                ", idMarca=" + idMarca +
                ", stockActual=" + stockActual +
                ", stockMinimo=" + stockMinimo +
                ", activo=" + activo +
                '}';
    }
}
