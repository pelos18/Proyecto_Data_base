package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "MARCAS")
@SequenceGenerator(name = "seq_marcas", sequenceName = "SEQ_MARCAS", allocationSize = 1)
public class Marca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_marcas")
    @Column(name = "ID_MARCA")
    private Long idMarca;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "NOMBRE", unique = true, nullable = false)
    private String nombre;
    
    // Constructores
    public Marca() {}
    
    public Marca(String nombre) {
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
