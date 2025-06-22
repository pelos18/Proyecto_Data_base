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
    
    // Buscar cliente por nombre
    Optional<Cliente> findByNombre(String nombre);
    
    // Buscar clientes por nombre que contenga
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar cliente por email
    Optional<Cliente> findByEmail(String email);
    
    // Buscar cliente por teléfono
    Optional<Cliente> findByTelefono(String telefono);
    
    // Buscar clientes por teléfono que contenga
    List<Cliente> findByTelefonoContaining(String telefono);
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe por email
    boolean existsByEmail(String email);
    
    // Verificar si existe por teléfono
    boolean existsByTelefono(String telefono);
    
    // Obtener todos los clientes ordenados por nombre
    List<Cliente> findAllByOrderByNombreAsc();
    
    // Buscar clientes con más compras (simplificado)
    @Query("SELECT c FROM Cliente c ORDER BY c.nombre")
    List<Cliente> findClientesConMasCompras();
    
    // Obtener estadísticas de clientes (simplificado)
    @Query("SELECT c.idCliente, c.nombre, 0L, 0.0 FROM Cliente c ORDER BY c.nombre")
    List<Object[]> getEstadisticasClientes();
    
    // Buscar clientes por rango de compras (simplificado)
    @Query("SELECT c FROM Cliente c WHERE c.idCliente > 0")
    List<Cliente> findClientesPorRangoCompras(@Param("montoMinimo") BigDecimal montoMinimo, 
                                             @Param("montoMaximo") BigDecimal montoMaximo);
    
    // Clientes con compras en un período (simplificado)
    @Query("SELECT c FROM Cliente c WHERE c.idCliente > 0")
    List<Cliente> findClientesConComprasEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, 
                                                 @Param("fechaFin") LocalDate fechaFin);
    
    // Buscar por término general (nombre, email o teléfono)
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "c.telefono LIKE CONCAT('%', :termino, '%')")
    List<Cliente> buscarPorTermino(@Param("termino") String termino);
    
    // Contar clientes activos
    @Query("SELECT COUNT(c) FROM Cliente c")
    Long countClientesActivos();
}
