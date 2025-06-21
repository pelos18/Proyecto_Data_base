package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Categoria;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.BusinessException;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.ValidationException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Obtener todas las categorías
    public List<Categoria> findAll() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }
    
    // Obtener categoría por ID
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Categoría con ID " + id + " no encontrada"));
    }
    
    // Crear nueva categoría
    public Categoria save(Categoria categoria) {
        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new ValidationException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        
        return categoriaRepository.save(categoria);
    }
    
    // Actualizar categoría
    public Categoria update(Categoria categoria) {
        Categoria categoriaExistente = findById(categoria.getIdCategoria());
        
        // Verificar si el nuevo nombre ya existe (si cambió)
        if (!categoriaExistente.getNombre().equals(categoria.getNombre()) && 
            categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new ValidationException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        
        return categoriaRepository.save(categoria);
    }
    
    // Eliminar categoría
    public void deleteById(Long id) {
        Categoria categoria = findById(id);
        
        // Verificar si la categoría tiene productos asociados
        Long productosAsociados = categoriaRepository.countByCategoriaIdCategoriaAndActivoTrue(id);
        if (productosAsociados > 0) {
            throw new BusinessException.RecursoEnUsoException(
                "Categoría '" + categoria.getNombre() + "'", 
                "tiene " + productosAsociados + " productos asociados"
            );
        }
        
        categoriaRepository.deleteById(id);
    }
    
    // Buscar categorías por nombre
    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Obtener categorías con productos activos
    public List<Categoria> findCategoriasConProductos() {
        return categoriaRepository.findCategoriasConProductosActivos();
    }
    
    // Obtener estadísticas de productos por categoría
    public List<Object[]> getEstadisticasProductosPorCategoria() {
        return categoriaRepository.countProductosPorCategoria();
    }
    
    // Obtener categorías sin productos
    public List<Categoria> findCategoriasSinProductos() {
        return categoriaRepository.findCategoriasSinProductos();
    }
    
    // Buscar categoría por nombre exacto
    public Categoria findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
            .orElseThrow(() -> new BusinessException("Categoría con nombre '" + nombre + "' no encontrada"));
    }
}
