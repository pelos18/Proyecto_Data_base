package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // --- MÉTODOS CORREGIDOS Y AÑADIDOS ---

    // Corregido para buscar a través de la relación de objetos
    @Query("SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria")
    List<Producto> findByCategoriaId(@Param("idCategoria") Long idCategoria);

    // Para los métodos que usan "activo" (campo ahora es Boolean)
    List<Producto> findByActivoTrue();

    // Para búsqueda general (usado en CatalogoController)
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoBarrasContaining(String nombre, String codigoBarras);

    // Para validación
    boolean existsByCodigoBarras(String codigoBarras);

    // Para reportes y estadísticas (usado en ProductoService y Dashboard)
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    Long countProductosActivos();

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo")
    List<Producto> findProductosConStockBajo();

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo")
    Long countProductosStockBajo();
}