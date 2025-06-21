package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USUARIOS")
@SequenceGenerator(name = "seq_usuarios", sequenceName = "SEQ_USUARIOS", allocationSize = 1)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuarios")
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "USUARIO", unique = true, nullable = false)
    private String usuario;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;
    
    @NotBlank
    @Size(max = 20)
    @Column(name = "ROL", nullable = false)
    private String rol;
    
    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String usuario, String password, String nombre, String rol) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
