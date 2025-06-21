package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

/**
 * Excepción para errores de lógica de negocio
 */
public class BusinessException extends SystemException {
    
    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super("BUSINESS_ERROR", message, cause);
    }
    
    // Excepciones específicas de negocio
    public static class OperacionNoPermitidaException extends BusinessException {
        public OperacionNoPermitidaException(String operacion, String razon) {
            super("No se puede realizar la operación '" + operacion + "'. Razón: " + razon);
        }
    }
    
    public static class EstadoInvalidoException extends BusinessException {
        public EstadoInvalidoException(String entidad, String estadoActual, String estadoRequerido) {
            super(String.format("Estado inválido para %s. Estado actual: %s, Estado requerido: %s", 
                               entidad, estadoActual, estadoRequerido));
        }
    }
    
    public static class ReglaNegocioVioladaException extends BusinessException {
        public ReglaNegocioVioladaException(String regla) {
            super("Regla de negocio violada: " + regla);
        }
    }
    
    public static class RecursoEnUsoException extends BusinessException {
        public RecursoEnUsoException(String recurso, String uso) {
            super("El recurso '" + recurso + "' está en uso: " + uso);
        }
    }
}
