package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importante

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    // --- CORRECCIÓN AQUÍ ---
    // Cambiamos el tipo de retorno a Optional<Proveedor>
    Optional<Proveedor> findByNombre(String nombre);
    
    // Método que usan otras partes del código
    List<Proveedor> findAllByOrderByNombreAsc();
}