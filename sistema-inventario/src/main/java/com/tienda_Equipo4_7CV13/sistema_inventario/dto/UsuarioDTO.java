package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {
    
    private Long idUsuario;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuario;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El rol es obligatorio")
    private String rol;
    
    private Boolean activo;
    
    // No incluimos password por seguridad
    
    // Constructores
    public UsuarioDTO() {}
    
    public UsuarioDTO(String usuario, String nombre, String rol) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
