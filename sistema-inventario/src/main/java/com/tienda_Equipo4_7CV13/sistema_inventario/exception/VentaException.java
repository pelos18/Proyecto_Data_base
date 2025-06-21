package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

/**
 * Excepción para errores relacionados con ventas
 */
public class VentaException extends SystemException {
    
    public VentaException(String message) {
        super("VENTA_ERROR", message);
    }
    
    public VentaException(String message, Throwable cause) {
        super("VENTA_ERROR", message, cause);
    }
    
    // Excepciones específicas de ventas
    public static class VentaNotFoundException extends VentaException {
        public VentaNotFoundException(Long ventaId) {
            super("Venta con ID " + ventaId + " no encontrada");
        }
    }
    
    public static class VentaYaConfirmadaException extends VentaException {
        public VentaYaConfirmadaException(Long ventaId) {
            super("La venta con ID " + ventaId + " ya ha sido confirmada");
        }
    }
    
    public static class VentaCanceladaException extends VentaException {
        public VentaCanceladaException(Long ventaId) {
            super("La venta con ID " + ventaId + " está cancelada");
        }
    }
    
    public static class VentaSinDetallesException extends VentaException {
        public VentaSinDetallesException() {
            super("No se puede procesar una venta sin detalles");
        }
    }
    
    public static class EstadoVentaInvalidoException extends VentaException {
        public EstadoVentaInvalidoException(String estadoActual, String estadoRequerido) {
            super(String.format("Estado de venta inválido. Estado actual: %s, Estado requerido: %s", 
                               estadoActual, estadoRequerido));
        }
    }
}
