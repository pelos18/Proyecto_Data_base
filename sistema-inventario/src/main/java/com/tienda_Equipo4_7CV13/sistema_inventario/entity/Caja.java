package com.tienda_Equipo4_7CV13.sistema_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CAJA")
@SequenceGenerator(name = "seq_caja", sequenceName = "SEQ_CAJA", allocationSize = 1)
public class Caja {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_caja")
    @Column(name = "ID_CAJA")
    private Long idCaja;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;
    
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha = LocalDate.now();
    
    @Column(name = "SALDO_INICIAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    
    @Column(name = "INGRESOS", precision = 12, scale = 2, nullable = false)
    private BigDecimal ingresos = BigDecimal.ZERO;
    
    @Column(name = "EGRESOS", precision = 12, scale = 2, nullable = false)
    private BigDecimal egresos = BigDecimal.ZERO;
    
    // SALDO_FINAL es calculado automáticamente por la base de datos
    @Column(name = "SALDO_FINAL", insertable = false, updatable = false)
    private BigDecimal saldoFinal;
    
    // Constructores
    public Caja() {}
    
    public Caja(Usuario usuario, BigDecimal saldoInicial) {
        this.usuario = usuario;
        this.saldoInicial = saldoInicial;
        this.fecha = LocalDate.now();
    }
    
    // Getters y Setters
    public Long getIdCaja() { return idCaja; }
    public void setIdCaja(Long idCaja) { this.idCaja = idCaja; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
    
    public BigDecimal getIngresos() { return ingresos; }
    public void setIngresos(BigDecimal ingresos) { this.ingresos = ingresos; }
    
    public BigDecimal getEgresos() { return egresos; }
    public void setEgresos(BigDecimal egresos) { this.egresos = egresos; }
    
    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }
}
