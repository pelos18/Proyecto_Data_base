package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    
    // Buscar marca por nombre
    Optional<Marca> findByNombre(String nombre);
    
    // Buscar marcas por nombre (contiene)
    List<Marca> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe una marca con ese nombre
    boolean existsByNombre(String nombre);
    
    // Buscar marcas ordenadas por nombre
    List<Marca> findAllByOrderByNombreAsc();
    
    // Contar productos por marca
    @Query("SELECT m.nombre, COUNT(p) FROM Marca m LEFT JOIN Producto p ON m.idMarca = p.marca.idMarca " +
           "WHERE p.activo = true GROUP BY m.idMarca, m.nombre ORDER BY COUNT(p) DESC")
    List<Object[]> countProductosPorMarca();
    
    // Obtener marcas con productos activos
    @Query("SELECT DISTINCT m FROM Marca m JOIN Producto p ON m.idMarca = p.marca.idMarca " +
           "WHERE p.activo = true ORDER BY m.nombre")
    List<Marca> findMarcasConProductosActivos();
    
    // Buscar marcas sin productos
    @Query("SELECT m FROM Marca m WHERE m.idMarca NOT IN " +
           "(SELECT DISTINCT p.marca.idMarca FROM Producto p WHERE p.activo = true)")
    List<Marca> findMarcasSinProductos();
}
