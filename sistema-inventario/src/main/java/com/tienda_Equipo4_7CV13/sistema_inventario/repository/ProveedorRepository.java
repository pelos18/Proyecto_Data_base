package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    // Buscar proveedores por nombre (contiene)
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar proveedores por teléfono
    List<Proveedor> findByTelefonoContaining(String telefono);
    
    // Buscar proveedores ordenados por nombre
    List<Proveedor> findAllByOrderByNombreAsc();
    
    // Obtener proveedores con más lotes suministrados
    @Query("SELECT p FROM Proveedor p JOIN LoteInventario l ON p.idProveedor = l.proveedor.idProveedor " +
           "GROUP BY p.idProveedor ORDER BY COUNT(l) DESC")
    List<Proveedor> findProveedoresConMasLotes();
    
    // Contar lotes por proveedor
    @Query("SELECT p.nombre, COUNT(l) FROM Proveedor p LEFT JOIN LoteInventario l ON p.idProveedor = l.proveedor.idProveedor " +
           "GROUP BY p.idProveedor, p.nombre ORDER BY COUNT(l) DESC")
    List<Object[]> countLotesPorProveedor();
    
    // Obtener proveedores con lotes activos (con stock)
    @Query("SELECT DISTINCT p FROM Proveedor p JOIN LoteInventario l ON p.idProveedor = l.proveedor.idProveedor " +
           "WHERE l.cantidad > 0 ORDER BY p.nombre")
    List<Proveedor> findProveedoresConLotesActivos();
    
    // Buscar proveedores sin lotes
    @Query("SELECT p FROM Proveedor p WHERE p.idProveedor NOT IN " +
           "(SELECT DISTINCT l.proveedor.idProveedor FROM LoteInventario l)")
    List<Proveedor> findProveedoresSinLotes();
    
    // Buscar por nombre o teléfono
    @Query("SELECT p FROM Proveedor p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "p.telefono LIKE CONCAT('%', :termino, '%')")
    List<Proveedor> buscarPorTermino(@Param("termino") String termino);
}
