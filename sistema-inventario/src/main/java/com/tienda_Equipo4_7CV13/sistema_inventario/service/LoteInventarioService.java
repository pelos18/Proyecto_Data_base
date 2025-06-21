package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.BusinessException;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.StockInsuficienteException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.LoteInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LoteInventarioService {
    
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;
    
    // Obtener todos los lotes
    public List<LoteInventario> findAll() {
        return loteInventarioRepository.findAll();
    }
    
    // Obtener lote por ID
    public LoteInventario findById(Long id) {
        return loteInventarioRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Lote con ID " + id + " no encontrado"));
    }
    
    // Crear nuevo lote
    public LoteInventario save(LoteInventario lote) {
        return loteInventarioRepository.save(lote);
    }
    
    // Actualizar lote
    public LoteInventario update(LoteInventario lote) {
        findById(lote.getIdLote()); // Verificar que existe
        return loteInventarioRepository.save(lote);
    }
    
    // Eliminar lote
    public void deleteById(Long id) {
        LoteInventario lote = findById(id);
        
        // Verificar que no tenga ventas asociadas
        // Esto se podría implementar con una consulta adicional
        
        loteInventarioRepository.deleteById(id);
    }
    
    // Obtener lotes por producto
    public List<LoteInventario> findByProducto(Long idProducto) {
        return loteInventarioRepository.findByProductoIdProducto(idProducto);
    }
    
    // Obtener lotes por proveedor
    public List<LoteInventario> findByProveedor(Long idProveedor) {
        return loteInventarioRepository.findByProveedorIdProveedor(idProveedor);
    }
    
    // Obtener lotes con stock disponible
    public List<LoteInventario> findLotesConStock() {
        return loteInventarioRepository.findByCantidadGreaterThan(0);
    }
    
    // Obtener lotes por producto con stock
    public List<LoteInventario> findLotesPorProductoConStock(Long idProducto) {
        return loteInventarioRepository.findByProductoIdProductoAndCantidadGreaterThan(idProducto, 0);
    }
    
    // Obtener lotes próximos a caducar
    public List<LoteInventario> findLotesProximosACaducar(int diasAnticipacion) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaLimite = fechaActual.plusDays(diasAnticipacion);
        return loteInventarioRepository.findLotesProximosACaducar(fechaActual, fechaLimite);
    }
    
    // Obtener lotes caducados
    public List<LoteInventario> findLotesCaducados() {
        return loteInventarioRepository.findLotesCaducados(LocalDate.now());
    }
    
    // Reducir stock del lote
    public void reducirStock(Long idLote, Integer cantidad) {
        LoteInventario lote = findById(idLote);
        
        if (lote.getCantidad() < cantidad) {
            throw new StockInsuficienteException(
                lote.getProducto().getIdProducto(),
                lote.getProducto().getNombre(),
                lote.getCantidad(),
                cantidad
            );
        }
        
        lote.setCantidad(lote.getCantidad() - cantidad);
        loteInventarioRepository.save(lote);
    }
    
    // Aumentar stock del lote
    public void aumentarStock(Long idLote, Integer cantidad) {
        LoteInventario lote = findById(idLote);
        lote.setCantidad(lote.getCantidad() + cantidad);
        loteInventarioRepository.save(lote);
    }
    
    // Obtener stock total por producto
    public List<Object[]> getStockTotalPorProducto() {
        return loteInventarioRepository.getStockTotalPorProducto();
    }
    
    // Obtener valor total del inventario
    public BigDecimal getValorTotalInventario() {
        BigDecimal valor = loteInventarioRepository.getValorTotalInventario();
        return valor != null ? valor : BigDecimal.ZERO;
    }
    
    // Obtener lotes por producto ordenados por caducidad (FIFO)
    public List<LoteInventario> findLotesPorProductoFIFO(Long idProducto) {
        return loteInventarioRepository.findLotesPorProductoOrdenadosPorCaducidad(idProducto);
    }
    
    // Obtener lotes sin movimiento
    public List<LoteInventario> findLotesSinMovimiento() {
        return loteInventarioRepository.findLotesSinMovimiento();
    }
    
    // Obtener lotes con mayor rotación
    public List<Object[]> findLotesConMayorRotacion() {
        return loteInventarioRepository.findLotesConMayorRotacion();
    }
    
    // Buscar lotes por rango de fechas de ingreso
    public List<LoteInventario> findLotesPorRangoFechaIngreso(LocalDate fechaInicio, LocalDate fechaFin) {
        return loteInventarioRepository.findLotesPorRangoFechaIngreso(fechaInicio, fechaFin);
    }
    
    // Buscar lotes por rango de precios
    public List<LoteInventario> findLotesPorRangoPrecios(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        return loteInventarioRepository.findLotesPorRangoPrecios(precioMinimo, precioMaximo);
    }
    
    // Validar lote antes de guardar
    private void validarLote(LoteInventario lote) {
        if (lote.getProducto() == null) {
            throw new BusinessException("El lote debe tener un producto asignado");
        }
        
        if (lote.getProveedor() == null) {
            throw new BusinessException("El lote debe tener un proveedor asignado");
        }
        
        if (lote.getCantidad() < 0) {
            throw new BusinessException("La cantidad no puede ser negativa");
        }
        
        if (lote.getPrecioCompra().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio de compra debe ser mayor a 0");
        }
        
        if (lote.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio de venta debe ser mayor a 0");
        }
    }
}
