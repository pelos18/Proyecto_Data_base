package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> findAll();
    List<Producto> findProductosActivos();
    Producto findById(Long id);
    Producto save(Producto producto);
    void delete(Long id);
    boolean existsByCodigoBarras(String codigoBarras);
    Long countProductosActivos();
    Long countProductosStockBajo();
    List<Producto> findProductosConStockBajo();
}
