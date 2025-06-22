package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAllByOrderByNombreAsc();
    boolean existsByNombre(String nombre);
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    Optional<Categoria> findByNombre(String nombre);
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.idCategoria = :id AND p.activo = true")
    Long countByCategoriaIdCategoriaAndActivoTrue(@Param("id") Long id);
    
    @Query("SELECT c FROM Categoria c WHERE EXISTS (SELECT p FROM Producto p WHERE p.categoria = c AND p.activo = true)")
    List<Categoria> findCategoriasConProductosActivos();
    
    @Query("SELECT c.nombre, COUNT(p) FROM Categoria c LEFT JOIN Producto p ON p.categoria = c GROUP BY c.nombre")
    List<Object[]> countProductosPorCategoria();
    
    @Query("SELECT c FROM Categoria c WHERE NOT EXISTS (SELECT p FROM Producto p WHERE p.categoria = c)")
    List<Categoria> findCategoriasSinProductos();
}
