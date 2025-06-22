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
    
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    List<Cliente> findAllByOrderByNombreAsc();
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
    List<Cliente> findByTelefonoContaining(String telefono);
    
    @Query("SELECT COUNT(c) FROM Cliente c")
    Long countTotalClientes();
    
    @Query("SELECT c FROM Cliente c ORDER BY c.nombre ASC")
    List<Cliente> findAllOrderByNombre();
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :termino, '%')) OR c.telefono LIKE CONCAT('%', :termino, '%')")
    List<Cliente> buscarPorTermino(@Param("termino") String termino);
}
