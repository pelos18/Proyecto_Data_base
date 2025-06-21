package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

import java.util.List;
import java.util.Map;

/**
 * Excepción para errores de validación
 */
public class ValidationException extends SystemException {
    
    private Map<String, List<String>> fieldErrors;
    
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }
    
    public ValidationException(String message, Map<String, List<String>> fieldErrors) {
        super("VALIDATION_ERROR", message);
        this.fieldErrors = fieldErrors;
    }
    
    public ValidationException(String field, String error) {
        super("VALIDATION_ERROR", "Error de validación en el campo: " + field);
        this.fieldErrors = Map.of(field, List.of(error));
    }
    
    // Excepciones específicas de validación
    public static class CampoRequeridoException extends ValidationException {
        public CampoRequeridoException(String campo) {
            super("El campo '" + campo + "' es requerido");
        }
    }
    
    public static class FormatoInvalidoException extends ValidationException {
        public FormatoInvalidoException(String campo, String formato) {
            super("El campo '" + campo + "' tiene un formato inválido. Formato esperado: " + formato);
        }
    }
    
    public static class ValorFueraDeRangoException extends ValidationException {
        public ValorFueraDeRangoException(String campo, Object valorMinimo, Object valorMaximo) {
            super("El valor del campo '" + campo + "' debe estar entre " + valorMinimo + " y " + valorMaximo);
        }
    }
    
    public static class LongitudInvalidaException extends ValidationException {
        public LongitudInvalidaException(String campo, int longitudMinima, int longitudMaxima) {
            super("El campo '" + campo + "' debe tener entre " + longitudMinima + " y " + longitudMaxima + " caracteres");
        }
    }
    
    // Getters
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
