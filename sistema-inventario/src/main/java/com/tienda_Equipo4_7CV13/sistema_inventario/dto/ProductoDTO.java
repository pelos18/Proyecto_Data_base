package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public class ProductoDTO {
    
    private Long idProducto;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    private String codigoBarras;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;
    
    private String nombreCategoria;
    
    @NotNull(message = "La marca es obligatoria")
    private Long idMarca;
    
    private String nombreMarca;
    
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;
    
    private Integer stockActual;
    
    private Boolean activo;
    
    private BigDecimal precioVenta;
    
    // Constructores
    public ProductoDTO() {}
    
    public ProductoDTO(String nombre, String descripcion, Long idCategoria, Long idMarca) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    
    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    
    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }
    
    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String nombreMarca) { this.nombreMarca = nombreMarca; }
    
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
}
