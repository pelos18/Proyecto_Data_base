package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

/**
 * Excepción lanzada cuando no hay suficiente stock para una operación
 */
public class StockInsuficienteException extends SystemException {
    
    private Long productoId;
    private String nombreProducto;
    private Integer stockDisponible;
    private Integer cantidadSolicitada;
    
    public StockInsuficienteException(Long productoId, String nombreProducto, 
                                    Integer stockDisponible, Integer cantidadSolicitada) {
        super("STOCK_INSUFICIENTE", 
              String.format("Stock insuficiente para el producto '%s'. Disponible: %d, Solicitado: %d", 
                           nombreProducto, stockDisponible, cantidadSolicitada));
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }
    
    public StockInsuficienteException(String nombreProducto, Integer stockDisponible) {
        super("STOCK_INSUFICIENTE", 
              String.format("Stock insuficiente para el producto '%s'. Stock disponible: %d", 
                           nombreProducto, stockDisponible));
        this.nombreProducto = nombreProducto;
        this.stockDisponible = stockDisponible;
    }
    
    // Getters
    public Long getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public Integer getStockDisponible() { return stockDisponible; }
    public Integer getCantidadSolicitada() { return cantidadSolicitada; }
}
