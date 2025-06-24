package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() { return productoRepository.findAll(); }
    
    @Override
    public List<Producto> findProductosActivos() { return productoRepository.findByActivoTrue(); }

    @Override
    public Producto findById(Long id) { return productoRepository.findById(id).orElse(null); }
    
    @Override
    public Producto save(Producto producto) { return productoRepository.save(producto); }

    @Override
    public void delete(Long id) { productoRepository.deleteById(id); }

    @Override
    public boolean existsByCodigoBarras(String codigoBarras) { return productoRepository.existsByCodigoBarras(codigoBarras); }

    @Override
    public Long countProductosActivos() { return productoRepository.countProductosActivos(); }

    @Override
    public Long countProductosStockBajo() { return productoRepository.countProductosStockBajo(); }

    @Override
    public List<Producto> findProductosConStockBajo() { return productoRepository.findProductosConStockBajo(); }
}