package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

/**
 * Excepción base del sistema de inventario
 */
public class SystemException extends RuntimeException {
    
    private String errorCode;
    private Object[] parameters;
    
    public SystemException(String message) {
        super(message);
    }
    
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SystemException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SystemException(String errorCode, String message, Object... parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }
    
    public SystemException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    // Getters
    public String getErrorCode() {
        return errorCode;
    }
    
    public Object[] getParameters() {
        return parameters;
    }
}
