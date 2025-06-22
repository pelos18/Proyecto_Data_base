package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
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

    public List<LoteInventario> findAll() {
        return loteInventarioRepository.findAll();
    }

    public LoteInventario findById(Long id) {
        return loteInventarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lote no encontrado con ID: " + id));
    }

    public LoteInventario save(LoteInventario lote) {
        return loteInventarioRepository.save(lote);
    }

    public void deleteById(Long id) {
        loteInventarioRepository.deleteById(id);
    }

    public List<LoteInventario> findByProductoId(Long productoId) {
        return loteInventarioRepository.findByProductoIdProducto(productoId);
    }

    public List<LoteInventario> findByProveedorId(Long proveedorId) {
        return loteInventarioRepository.findByProveedorIdProveedor(proveedorId);
    }

    public List<LoteInventario> findLotesConStock() {
        return loteInventarioRepository.findByCantidadGreaterThan(0);
    }

    public Double getPrecioVentaByProductoId(Long productoId) {
        return loteInventarioRepository.findPrecioVentaByProductoId(productoId);
    }

    public List<LoteInventario> findLotesProximosACaducar(int dias) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaLimite = fechaActual.plusDays(dias);
        return loteInventarioRepository.findLotesProximosACaducar(fechaActual, fechaLimite);
    }

    public List<LoteInventario> findLotesCaducados() {
        return loteInventarioRepository.findLotesCaducados(LocalDate.now());
    }

    public Long countLotesActivos() {
        return loteInventarioRepository.countLotesActivos();
    }

    public Long countLotesProximosAVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return loteInventarioRepository.countLotesProximosAVencer(fechaLimite);
    }

    public BigDecimal getValorTotalInventario() {
        return loteInventarioRepository.getValorTotalInventario();
    }

    public List<LoteInventario> findLotesPorProductoOrdenadosPorCaducidad(Long idProducto) {
        return loteInventarioRepository.findLotesPorProductoOrdenadosPorCaducidad(idProducto);
    }

    public List<LoteInventario> findLotesPorRangoFechaIngreso(LocalDate fechaInicio, LocalDate fechaFin) {
        return loteInventarioRepository.findLotesPorRangoFechaIngreso(fechaInicio, fechaFin);
    }

    public List<LoteInventario> findLotesPorRangoPrecios(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        return loteInventarioRepository.findLotesPorRangoPrecios(precioMinimo, precioMaximo);
    }

    public void reducirStock(Long idLote, Integer cantidad) {
        LoteInventario lote = findById(idLote);
        if (lote.getCantidad() >= cantidad) {
            lote.setCantidad(lote.getCantidad() - cantidad);
            save(lote);
        } else {
            throw new RuntimeException("Stock insuficiente en el lote");
        }
    }
}
