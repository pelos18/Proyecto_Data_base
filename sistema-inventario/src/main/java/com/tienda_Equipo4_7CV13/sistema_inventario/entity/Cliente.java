package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "CLIENTES")
@SequenceGenerator(name = "seq_clientes", sequenceName = "SEQ_CLIENTES", allocationSize = 1)
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clientes")
    @Column(name = "ID_CLIENTE")
    private Long idCliente;
    
    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;
    
    @Size(max = 20)
    @Column(name = "TELEFONO")
    private String telefono;
    
    @Email
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    
    @Size(max = 255)
    @Column(name = "DIRECCION")
    private String direccion;
    
    // Constructores
    public Cliente() {}
    
    public Cliente(String nombre, String telefono, String email, String direccion) {
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
}
