package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "marcas")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMarca;
    
    // ... el resto de la clase como la tenías, con el constructor con parámetros.
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    public Marca() {}
    public Marca(String nombre) { this.nombre = nombre; }
    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}