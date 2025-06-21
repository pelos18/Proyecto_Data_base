package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para toda la aplicación
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductoNotFound(ProductoNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Producto no encontrado",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleStockInsuficiente(StockInsuficienteException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Stock insuficiente",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(VentaException.class)
    public ResponseEntity<ErrorResponse> handleVentaException(VentaException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error en venta",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioException(UsuarioException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        
        // Determinar el status HTTP según el tipo de excepción
        if (ex instanceof UsuarioException.UsuarioNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof UsuarioException.CredencialesInvalidasException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof UsuarioException.PermisosDenegadosException) {
            status = HttpStatus.FORBIDDEN;
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            "Error de usuario",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de validación",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now(),
            ex.getFieldErrors()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, List<String>> fieldErrors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            
            fieldErrors.computeIfAbsent(fieldName, k -> new java.util.ArrayList<>()).add(errorMessage);
        });
        
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de validación",
            "Los datos enviados no son válidos",
            request.getDescription(false),
            LocalDateTime.now(),
            fieldErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de negocio",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(SystemException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error del sistema",
            ex.getMessage(),
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error interno del servidor",
            "Ha ocurrido un error inesperado",
            request.getDescription(false),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Clases para las respuestas de error
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private String path;
        private LocalDateTime timestamp;
        
        public ErrorResponse(int status, String error, String message, String path, LocalDateTime timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
            this.timestamp = timestamp;
        }
        
        // Getters y Setters
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
    
    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, List<String>> fieldErrors;
        
        public ValidationErrorResponse(int status, String error, String message, String path, 
                                     LocalDateTime timestamp, Map<String, List<String>> fieldErrors) {
            super(status, error, message, path, timestamp);
            this.fieldErrors = fieldErrors;
        }
        
        public Map<String, List<String>> getFieldErrors() { return fieldErrors; }
        public void setFieldErrors(Map<String, List<String>> fieldErrors) { this.fieldErrors = fieldErrors; }
    }
}
