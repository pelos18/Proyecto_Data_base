package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "NOTIFICACIONES")
@SequenceGenerator(name = "seq_notificaciones", sequenceName = "SEQ_NOTIFICACIONES", allocationSize = 1)
public class Notificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notificaciones")
    @Column(name = "ID_NOTIFICACION")
    private Long idNotificacion;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO")
    private Producto producto;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "TIPO", nullable = false)
    private String tipo;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "MENSAJE", nullable = false)
    private String mensaje;
    
    @Column(name = "FECHA_CREACION", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();
    
    @Column(name = "LEIDA", nullable = false)
    private Boolean leida = false;
    
    // Constructores
    public Notificacion() {}
    
    public Notificacion(Usuario usuario, String tipo, String mensaje) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDate.now();
        this.leida = false;
    }
    
    public Notificacion(Usuario usuario, Producto producto, String tipo, String mensaje) {
        this(usuario, tipo, mensaje);
        this.producto = producto;
    }
    
    // Getters y Setters
    public Long getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Long idNotificacion) { this.idNotificacion = idNotificacion; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Boolean getLeida() { return leida; }
    public void setLeida(Boolean leida) { this.leida = leida; }
}
