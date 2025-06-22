package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    // Buscar proveedor por nombre
    Optional<Proveedor> findByNombre(String nombre);
    
    // Buscar proveedores por nombre que contenga
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar proveedores por teléfono
    Optional<Proveedor> findByTelefono(String telefono);
    
    // Buscar proveedores activos (si tienes campo activo)
    // List<Proveedor> findByActivoTrue();
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe por teléfono
    boolean existsByTelefono(String telefono);
}
