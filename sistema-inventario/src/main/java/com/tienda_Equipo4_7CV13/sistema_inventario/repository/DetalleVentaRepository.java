package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
    List<DetalleVenta> findByVentaIdVenta(Long ventaId);
    
    @Query("SELECT p.nombre as nombre, SUM(dv.cantidad) as cantidad " +
           "FROM DetalleVenta dv JOIN dv.producto p JOIN dv.venta v " +
           "WHERE v.estado = 'COMPLETADA' " +
           "GROUP BY p.idProducto, p.nombre " +
           "ORDER BY SUM(dv.cantidad) DESC")
    List<Map<String, Object>> findProductosMasVendidos();
}
