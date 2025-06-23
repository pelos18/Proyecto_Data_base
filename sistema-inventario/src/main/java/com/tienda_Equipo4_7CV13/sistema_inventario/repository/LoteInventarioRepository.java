package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteInventarioRepository extends JpaRepository<LoteInventario, Long> {
    
    List<LoteInventario> findByFechaCaducidadBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<LoteInventario> findByFechaCaducidadBefore(LocalDate fecha);
    long countByFechaCaducidadBefore(LocalDate fecha);
    
    @Query("SELECT COUNT(l) FROM LoteInventario l WHERE l.cantidad > 0")
    Long countLotesActivos();
    
    @Query("SELECT COUNT(l) FROM LoteInventario l WHERE l.fechaCaducidad <= :fechaLimite AND l.cantidad > 0")
    Long countLotesProximosAVencer(@Param("fechaLimite") LocalDate fechaLimite);
    
    @Query("SELECT l.precioVenta FROM LoteInventario l WHERE l.producto.idProducto = :productoId AND l.cantidad > 0 ORDER BY l.fechaCaducidad ASC")
    Double findPrecioVentaByProductoId(@Param("productoId") Long productoId);
    
    @Query("SELECT COALESCE(SUM(l.cantidad * l.precioVenta), 0) FROM LoteInventario l WHERE l.cantidad > 0")
    BigDecimal getValorTotalInventario();
}
