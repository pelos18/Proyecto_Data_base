package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    boolean existsByCodigoBarras(String codigoBarras);
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    Long countProductosActivos();
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stockMinimo >= p.stockActual")
    Long countProductosStockBajo();
    
    @Query("SELECT p FROM Producto p WHERE p.stockMinimo >= p.stockActual")
    List<Producto> findProductosConStockBajo();
    
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoBarrasContaining(String nombre, String codigoBarras);
    List<Producto> findByCategoriaIdCategoria(Long categoriaId);
    List<Producto> findByMarcaIdMarca(Long marcaId);
}
