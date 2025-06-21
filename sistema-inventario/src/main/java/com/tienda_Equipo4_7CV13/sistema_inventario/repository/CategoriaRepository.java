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
    
    // Buscar categoría por nombre
    Optional<Categoria> findByNombre(String nombre);
    
    // Buscar categorías por nombre (contiene)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe una categoría con ese nombre
    boolean existsByNombre(String nombre);
    
    // Buscar categorías ordenadas por nombre
    List<Categoria> findAllByOrderByNombreAsc();
    
    // Contar productos por categoría
    @Query("SELECT c.nombre, COUNT(p) FROM Categoria c LEFT JOIN Producto p ON c.idCategoria = p.categoria.idCategoria " +
           "WHERE p.activo = true GROUP BY c.idCategoria, c.nombre ORDER BY COUNT(p) DESC")
    List<Object[]> countProductosPorCategoria();
    
    // Obtener categorías con productos activos
    @Query("SELECT DISTINCT c FROM Categoria c JOIN Producto p ON c.idCategoria = p.categoria.idCategoria " +
           "WHERE p.activo = true ORDER BY c.nombre")
    List<Categoria> findCategoriasConProductosActivos();
    
    // Buscar categorías sin productos
    @Query("SELECT c FROM Categoria c WHERE c.idCategoria NOT IN " +
           "(SELECT DISTINCT p.categoria.idCategoria FROM Producto p WHERE p.activo = true)")
    List<Categoria> findCategoriasSinProductos();
}
