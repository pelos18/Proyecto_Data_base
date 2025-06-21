package com.tienda_Equipo4_7CV13.sistema_inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class ClienteDTO {
    
    private Long idCliente;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombre;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccion;
    
    // Para estadísticas
    private Integer totalCompras;
    private java.math.BigDecimal totalGastado;
    
    // Constructores
    public ClienteDTO() {}
    
    public ClienteDTO(String nombre, String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }
    
    // Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public Integer getTotalCompras() { return totalCompras; }
    public void setTotalCompras(Integer totalCompras) { this.totalCompras = totalCompras; }
    
    public java.math.BigDecimal getTotalGastado() { return totalGastado; }
    public void setTotalGastado(java.math.BigDecimal totalGastado) { this.totalGastado = totalGastado; }
}
