package com.tienda_Equipo4_7CV13.sistema_inventario.exception;

/**
 * Excepción para errores relacionados con usuarios
 */
public class UsuarioException extends SystemException {
    
    public UsuarioException(String message) {
        super("USUARIO_ERROR", message);
    }
    
    public UsuarioException(String message, Throwable cause) {
        super("USUARIO_ERROR", message, cause);
    }
    
    // Excepciones específicas de usuarios
    public static class UsuarioNotFoundException extends UsuarioException {
        public UsuarioNotFoundException(Long usuarioId) {
            super("Usuario con ID " + usuarioId + " no encontrado");
        }
        
        public UsuarioNotFoundException(String username) {
            super("Usuario '" + username + "' no encontrado");
        }
    }
    
    public static class UsuarioInactivoException extends UsuarioException {
        public UsuarioInactivoException(String username) {
            super("El usuario '" + username + "' está inactivo");
        }
    }
    
    public static class UsuarioYaExisteException extends UsuarioException {
        public UsuarioYaExisteException(String username) {
            super("El usuario '" + username + "' ya existe en el sistema");
        }
    }
    
    public static class CredencialesInvalidasException extends UsuarioException {
        public CredencialesInvalidasException() {
            super("Credenciales inválidas");
        }
    }
    
    public static class PermisosDenegadosException extends UsuarioException {
        public PermisosDenegadosException(String operacion) {
            super("No tiene permisos para realizar la operación: " + operacion);
        }
    }
}
