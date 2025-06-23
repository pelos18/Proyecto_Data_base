package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReporteInventarioService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteInventarioService.class);

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;

    public Map<String, Object> generarReporteCompleto() {
        logger.info("Iniciando generación de reporte completo de inventario");
        
        Map<String, Object> reporte = new HashMap<>();
        
        try {
            // Información general
            reporte.put("fechaReporte", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            reporte.put("horaReporte", java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            
            // Estadísticas básicas
            reporte.put("totalProductos", productoRepository.count());
            reporte.put("totalCategorias", categoriaRepository.count());
            reporte.put("totalMarcas", marcaRepository.count());
            
            // Productos activos
            List<Producto> productosActivos = productoRepository.findByActivoTrue();
            reporte.put("productosActivos", productosActivos);
            reporte.put("cantidadProductosActivos", productosActivos.size());
            
            // Lotes de inventario
            List<LoteInventario> todosLotes = loteInventarioRepository.findAll();
            reporte.put("totalLotes", todosLotes.size());
            
            // Productos con información básica
            List<Map<String, Object>> productosConInfo = obtenerProductosBasicos();
            reporte.put("productosConInfo", productosConInfo);
            
            logger.info("Reporte generado exitosamente con {} productos", productosActivos.size());
            
        } catch (Exception e) {
            logger.error("Error al generar reporte completo: ", e);
            reporte.put("error", "Error al generar reporte: " + e.getMessage());
        }
        
        return reporte;
    }

    private List<Map<String, Object>> obtenerProductosBasicos() {
        List<Map<String, Object>> productos = new ArrayList<>();
        
        try {
            List<Producto> productosActivos = productoRepository.findByActivoTrue();
            
            for (Producto producto : productosActivos) {
                Map<String, Object> productoInfo = new HashMap<>();
                productoInfo.put("id", producto.getIdProducto());
                productoInfo.put("codigoBarras", producto.getCodigoBarras());
                productoInfo.put("nombre", producto.getNombre());
                productoInfo.put("descripcion", producto.getDescripcion());
                productoInfo.put("stockActual", producto.getStockActual());
                productoInfo.put("stockMinimo", producto.getStockMinimo());
                
                // Información de categoría y marca
                if (producto.getCategoria() != null) {
                    productoInfo.put("categoria", producto.getCategoria().getNombre());
                } else {
                    productoInfo.put("categoria", "Sin categoría");
                }
                
                if (producto.getMarca() != null) {
                    productoInfo.put("marca", producto.getMarca().getNombre());
                } else {
                    productoInfo.put("marca", "Sin marca");
                }
                
                productos.add(productoInfo);
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener productos básicos: ", e);
        }
        
        return productos;
    }

    public BigDecimal calcularValorTotalInventario() {
        try {
            // Cálculo básico del valor total
            List<LoteInventario> lotes = loteInventarioRepository.findAll();
            BigDecimal total = BigDecimal.ZERO;
        
            for (LoteInventario lote : lotes) {
                if (lote.getCantidad() != null && lote.getPrecioCompra() != null) {
                    BigDecimal valorLote = BigDecimal.valueOf(lote.getCantidad())
                        .multiply(lote.getPrecioCompra());
                    total = total.add(valorLote);
                }
            }
        
            return total;
        } catch (Exception e) {
            logger.error("Error al calcular valor total del inventario: ", e);
            return BigDecimal.ZERO;
        }
    }

    public List<Producto> obtenerReporteCompleto() {
        return productoRepository.findByActivoTrue();
    }

    public List<LoteInventario> obtenerLotesPorProducto(Long productoId) {
        return loteInventarioRepository.findByProductoIdProducto(productoId);
    }

    public byte[] generarInventarioPDF() {
        String contenido = "REPORTE DE INVENTARIO\n" +
                          "Fecha: " + LocalDate.now() + "\n" +
                          "Este es un reporte básico de inventario.\n";
        
        return contenido.getBytes();
    }
}
