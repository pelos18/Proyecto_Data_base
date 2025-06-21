package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CATEGORIAS")
@SequenceGenerator(name = "seq_categorias", sequenceName = "SEQ_CATEGORIAS", allocationSize = 1)
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categorias")
    @Column(name = "ID_CATEGORIA")
    private Long idCategoria;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "NOMBRE", unique = true, nullable = false)
    private String nombre;
    
    @Size(max = 255)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    // Constructores
    public Categoria() {}
    
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
