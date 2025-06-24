package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Métodos básicos usando valores numéricos para Oracle (1 = activo, 0 = inactivo)
    @Query("SELECT p FROM Producto p WHERE p.activo = 1")
    List<Producto> findByActivoTrue();
    
    @Query("SELECT p FROM Producto p WHERE p.idCategoria = :categoriaId AND p.activo = 1")
    List<Producto> findByCategoriaIdCategoriaAndActivoTrue(@Param("categoriaId") Long categoriaId);
    
    List<Producto> findByIdCategoria(Long categoriaId);
    List<Producto> findByIdMarca(Long marcaId);
    
    // Métodos de búsqueda
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoBarrasContaining(String nombre, String codigoBarras);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByCodigoBarras(String codigoBarras);
    
    // Métodos de conteo usando valores numéricos
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = 1")
    Long countProductosActivos();
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stockMinimo >= p.stockActual AND p.activo = 1")
    Long countProductosStockBajo();
    
    // Método para productos con stock bajo usando valores numéricos
    @Query("SELECT p FROM Producto p WHERE p.stockMinimo >= p.stockActual AND p.activo = 1")
    List<Producto> findProductosConStockBajo();
}
