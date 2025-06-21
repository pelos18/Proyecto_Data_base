package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar cliente por email
    Optional<Cliente> findByEmail(String email);
    
    // Buscar clientes por nombre (contiene)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar clientes por teléfono
    List<Cliente> findByTelefonoContaining(String telefono);
    
    // Verificar si existe un cliente con ese email
    boolean existsByEmail(String email);
    
    // Buscar clientes ordenados por nombre
    List<Cliente> findAllByOrderByNombreAsc();
    
    // Obtener clientes con más compras
    @Query("SELECT c FROM Cliente c JOIN Venta v ON c.idCliente = v.cliente.idCliente " +
           "GROUP BY c.idCliente ORDER BY COUNT(v) DESC")
    List<Cliente> findClientesConMasCompras();
    
    // Obtener estadísticas de cliente
    @Query("SELECT c.idCliente, c.nombre, COUNT(v), COALESCE(SUM(v.total), 0) " +
           "FROM Cliente c LEFT JOIN Venta v ON c.idCliente = v.cliente.idCliente " +
           "WHERE v.estado = 'ACEPTADA' OR v.estado IS NULL " +
           "GROUP BY c.idCliente, c.nombre ORDER BY COALESCE(SUM(v.total), 0) DESC")
    List<Object[]> getEstadisticasClientes();
    
    // Buscar clientes por rango de compras
    @Query("SELECT c FROM Cliente c JOIN Venta v ON c.idCliente = v.cliente.idCliente " +
           "WHERE v.total BETWEEN :montoMinimo AND :montoMaximo AND v.estado = 'ACEPTADA' " +
           "GROUP BY c.idCliente")
    List<Cliente> findClientesPorRangoCompras(@Param("montoMinimo") BigDecimal montoMinimo, 
                                             @Param("montoMaximo") BigDecimal montoMaximo);
    
    // Clientes con compras en un período
    @Query("SELECT DISTINCT c FROM Cliente c JOIN Venta v ON c.idCliente = v.cliente.idCliente " +
           "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<Cliente> findClientesConComprasEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, 
                                                 @Param("fechaFin") LocalDate fechaFin);
    
    // Buscar por nombre, email o teléfono
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "c.telefono LIKE CONCAT('%', :termino, '%')")
    List<Cliente> buscarPorTermino(@Param("termino") String termino);
}
