package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Estadísticas básicas
            stats.put("totalProductos", productoRepository.countProductosActivos());
            stats.put("totalClientes", clienteRepository.countTotalClientes());
            stats.put("stockBajo", productoRepository.countProductosStockBajo());
            
            // Ventas del día
            BigDecimal ventasHoy = ventaRepository.findVentasDelDia();
            stats.put("ventasHoy", ventasHoy != null ? ventasHoy.doubleValue() : 0.0);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/ventas-semana")
    public ResponseEntity<Map<String, Object>> getVentasSemana() {
        try {
            Map<String, Object> data = new HashMap<>();
            
            // Obtener ventas de los últimos 7 días
            LocalDateTime hoy = LocalDateTime.now();
            LocalDateTime hace7Dias = hoy.minusDays(7);
            
            List<Double> ventasPorDia = List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            // Aquí podrías implementar la lógica real para obtener ventas por día
            
            data.put("ventas", ventasPorDia);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<Map<String, Object>> getProductosMasVendidos() {
        try {
            Map<String, Object> data = new HashMap<>();
            
            List<Map<String, Object>> productos = detalleVentaRepository.findProductosMasVendidos();
            
            List<String> nombres = productos.stream()
                .limit(5)
                .map(p -> (String) p.get("nombre"))
                .toList();
                
            List<Long> cantidades = productos.stream()
                .limit(5)
                .map(p -> (Long) p.get("cantidad"))
                .toList();
            
            data.put("nombres", nombres);
            data.put("cantidades", cantidades);
            
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/reporte-ventas")
    public ResponseEntity<Map<String, Object>> getReporteVentas() {
        try {
            Map<String, Object> reporte = new HashMap<>();
            
            BigDecimal ventasHoy = ventaRepository.findVentasDelDia();
            BigDecimal ventasMes = ventaRepository.findVentasDelMes();
            Long totalTransacciones = ventaRepository.countVentasCompletadas();
            
            reporte.put("ventasHoy", ventasHoy != null ? ventasHoy.doubleValue() : 0.0);
            reporte.put("ventasMes", ventasMes != null ? ventasMes.doubleValue() : 0.0);
            reporte.put("totalTransacciones", totalTransacciones != null ? totalTransacciones : 0L);
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/reporte-ganancias")
    public ResponseEntity<Map<String, Object>> getReporteGanancias() {
        try {
            Map<String, Object> reporte = new HashMap<>();
            
            // Aquí implementarías el cálculo real de ganancias
            // basado en precio de venta vs precio de compra
            BigDecimal ventasMes = ventaRepository.findVentasDelMes();
            double gananciaBruta = ventasMes != null ? ventasMes.doubleValue() : 0.0;
            double costos = gananciaBruta * 0.7; // Estimación del 70% de costos
            double gananciaNeta = gananciaBruta - costos;
            double margen = gananciaBruta > 0 ? (gananciaNeta / gananciaBruta) * 100 : 0;
            
            reporte.put("gananciaBruta", gananciaBruta);
            reporte.put("costos", costos);
            reporte.put("gananciaNeta", gananciaNeta);
            reporte.put("margen", Math.round(margen * 100.0) / 100.0);
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/inventario-alertas")
    public ResponseEntity<Map<String, Object>> getInventarioAlertas() {
        try {
            Map<String, Object> alertas = new HashMap<>();
            
            // Productos por vencer (próximos 30 días)
            LocalDate fechaLimite = LocalDate.now().plusDays(30);
            Long productosVencer = loteInventarioRepository.countLotesProximosAVencer(fechaLimite);
            
            // Stock mínimo
            Long stockMinimo = productoRepository.countProductosStockBajo();
            
            // Lotes activos
            Long lotesActivos = loteInventarioRepository.countLotesActivos();
            
            alertas.put("productosVencer", productosVencer != null ? productosVencer : 0L);
            alertas.put("stockMinimo", stockMinimo != null ? stockMinimo : 0L);
            alertas.put("lotesActivos", lotesActivos != null ? lotesActivos : 0L);
            
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
