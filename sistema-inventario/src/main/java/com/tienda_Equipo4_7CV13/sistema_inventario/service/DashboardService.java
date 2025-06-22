package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Producto;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ProductoRepository;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.VentaRepository;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public Long getTotalProductos() {
        try {
            logger.info("=== CONSULTANDO TOTAL DE PRODUCTOS ===");
            Long total = productoRepository.count();
            logger.info("Total de productos encontrados en BD: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al consultar total de productos: ", e);
            return 0L;
        }
    }
    
    public Long getTotalClientes() {
        try {
            logger.info("=== CONSULTANDO TOTAL DE CLIENTES ===");
            Long total = clienteRepository.count();
            logger.info("Total de clientes encontrados en BD: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al consultar total de clientes: ", e);
            return 0L;
        }
    }
    
    public Long getVentasHoy() {
        try {
            logger.info("=== CONSULTANDO VENTAS DE HOY ===");
            LocalDate hoy = LocalDate.now();
            logger.info("Buscando ventas para la fecha: {}", hoy);
            
            List<Venta> ventasHoy = ventaRepository.findByFechaVenta(hoy);
            Long total = (long) ventasHoy.size();
            
            logger.info("Ventas encontradas para hoy: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al consultar ventas de hoy: ", e);
            return 0L;
        }
    }
    
    public BigDecimal getIngresosHoy() {
        try {
            logger.info("=== CONSULTANDO INGRESOS DE HOY ===");
            LocalDate hoy = LocalDate.now();
            logger.info("Calculando ingresos para la fecha: {}", hoy);
            
            List<Venta> ventasHoy = ventaRepository.findByFechaVenta(hoy);
            BigDecimal total = ventasHoy.stream()
                .filter(v -> "ACEPTADA".equals(v.getEstado()))
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            logger.info("Ingresos calculados para hoy: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al consultar ingresos de hoy: ", e);
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getIngresosMes() {
        try {
            logger.info("=== CONSULTANDO INGRESOS DEL MES ===");
            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            LocalDate finMes = LocalDate.now();
            logger.info("Calculando ingresos del {} al {}", inicioMes, finMes);
            
            List<Venta> ventasMes = ventaRepository.findByFechaVentaBetween(inicioMes, finMes);
            BigDecimal total = ventasMes.stream()
                .filter(v -> "ACEPTADA".equals(v.getEstado()))
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            logger.info("Ingresos calculados para el mes: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al consultar ingresos del mes: ", e);
            return BigDecimal.ZERO;
        }
    }
    
    public List<Producto> getProductosStockBajo() {
        try {
            logger.info("=== CONSULTANDO PRODUCTOS CON STOCK BAJO ===");
            List<Producto> todosProductos = productoRepository.findAll();
            List<Producto> productosStockBajo = todosProductos.stream()
                .filter(p -> p.getStockActual() <= p.getStockMinimo())
                .collect(Collectors.toList());
            
            logger.info("Productos con stock bajo encontrados: {}", productosStockBajo.size());
            return productosStockBajo;
        } catch (Exception e) {
            logger.error("Error al consultar productos con stock bajo: ", e);
            return List.of();
        }
    }
    
    public List<Venta> getVentasRecientes() {
        try {
            logger.info("=== CONSULTANDO VENTAS RECIENTES ===");
            List<Venta> todasVentas = ventaRepository.findAll();
            List<Venta> ventasRecientes = todasVentas.stream()
                .sorted((v1, v2) -> {
                    // Ordenar por fecha descendente, luego por ID descendente
                    int fechaComparison = v2.getFechaVenta().compareTo(v1.getFechaVenta());
                    if (fechaComparison != 0) {
                        return fechaComparison;
                    }
                    return v2.getIdVenta().compareTo(v1.getIdVenta());
                })
                .limit(10)
                .collect(Collectors.toList());
            
            logger.info("Ventas recientes encontradas: {}", ventasRecientes.size());
            return ventasRecientes;
        } catch (Exception e) {
            logger.error("Error al consultar ventas recientes: ", e);
            return List.of();
        }
    }
    
    public void verificarConexionBaseDatos() {
        try {
            logger.info("=== VERIFICANDO CONEXIÓN A BASE DE DATOS ===");
            
            // Verificar tablas principales
            Long productos = productoRepository.count();
            Long clientes = clienteRepository.count();
            Long ventas = ventaRepository.count();
            
            logger.info("RESUMEN DE BASE DE DATOS:");
            logger.info("- Productos: {}", productos);
            logger.info("- Clientes: {}", clientes);
            logger.info("- Ventas: {}", ventas);
            
            if (productos == 0 && clientes == 0 && ventas == 0) {
                logger.warn("⚠️  LA BASE DE DATOS PARECE ESTAR VACÍA");
                logger.warn("⚠️  Verifica que estés conectado a la base de datos correcta");
            } else {
                logger.info("✅ BASE DE DATOS CONECTADA Y CON DATOS");
            }
            
        } catch (Exception e) {
            logger.error("❌ ERROR DE CONEXIÓN A BASE DE DATOS: ", e);
        }
    }
}
