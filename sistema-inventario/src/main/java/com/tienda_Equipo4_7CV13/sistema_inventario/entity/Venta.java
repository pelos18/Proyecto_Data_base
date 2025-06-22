package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venta_seq")
    @SequenceGenerator(name = "venta_seq", sequenceName = "SEQ_VENTAS", allocationSize = 1)
    @Column(name = "id_venta")
    private Long idVenta;

    // CAMBIAR DE Date A LocalDateTime PARA COINCIDIR CON TIMESTAMP
    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    // AGREGAR CAMPO ESTADO QUE FALTABA
    @Column(name = "estado", length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles;

    @PrePersist
    public void prePersist() {
        if (fechaVenta == null) {
            fechaVenta = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}
