package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Marca;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.BusinessException;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.ValidationException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MarcaService {
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    // Obtener todas las marcas
    public List<Marca> findAll() {
        return marcaRepository.findAllByOrderByNombreAsc();
    }
    
    // Obtener marca por ID
    public Marca findById(Long id) {
        return marcaRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Marca con ID " + id + " no encontrada"));
    }
    
    // Crear nueva marca
    public Marca save(Marca marca) {
        // Validar que no exista una marca con el mismo nombre
        if (marcaRepository.existsByNombre(marca.getNombre())) {
            throw new ValidationException("Ya existe una marca con el nombre: " + marca.getNombre());
        }
        
        return marcaRepository.save(marca);
    }
    
    // Actualizar marca
    public Marca update(Marca marca) {
        Marca marcaExistente = findById(marca.getIdMarca());
        
        // Verificar si el nuevo nombre ya existe (si cambió)
        if (!marcaExistente.getNombre().equals(marca.getNombre()) && 
            marcaRepository.existsByNombre(marca.getNombre())) {
            throw new ValidationException("Ya existe una marca con el nombre: " + marca.getNombre());
        }
        
        return marcaRepository.save(marca);
    }
    
    // Eliminar marca
    public void deleteById(Long id) {
        Marca marca = findById(id);
        
        // Verificar si la marca tiene productos asociados
        Long productosAsociados = marcaRepository.countByMarcaIdMarcaAndActivoTrue(id);
        if (productosAsociados > 0) {
            throw new BusinessException.RecursoEnUsoException(
                "Marca '" + marca.getNombre() + "'", 
                "tiene " + productosAsociados + " productos asociados"
            );
        }
        
        marcaRepository.deleteById(id);
    }
    
    // Buscar marcas por nombre
    public List<Marca> buscarPorNombre(String nombre) {
        return marcaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Obtener marcas con productos activos
    public List<Marca> findMarcasConProductos() {
        return marcaRepository.findMarcasConProductosActivos();
    }
    
    // Obtener estadísticas de productos por marca
    public List<Object[]> getEstadisticasProductosPorMarca() {
        return marcaRepository.countProductosPorMarca();
    }
    
    // Obtener marcas sin productos
    public List<Marca> findMarcasSinProductos() {
        return marcaRepository.findMarcasSinProductos();
    }
    
    // Buscar marca por nombre exacto
    public Marca findByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre)
            .orElseThrow(() -> new BusinessException("Marca con nombre '" + nombre + "' no encontrada"));
    }
}
