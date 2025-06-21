package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PROVEEDORES")
@SequenceGenerator(name = "seq_proveedores", sequenceName = "SEQ_PROVEEDORES", allocationSize = 1)
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proveedores")
    @Column(name = "ID_PROVEEDOR")
    private Long idProveedor;
    
    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;
    
    @Size(max = 20)
    @Column(name = "TELEFONO")
    private String telefono;
    
    @Size(max = 255)
    @Column(name = "DIRECCION")
    private String direccion;
    
    // Constructores
    public Proveedor() {}
    
    public Proveedor(String nombre, String telefono, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    // Getters y Setters
    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
