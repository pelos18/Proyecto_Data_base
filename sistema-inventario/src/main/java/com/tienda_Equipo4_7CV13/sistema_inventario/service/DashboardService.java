package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.dto.DashboardDTO;
import com.tienda_Equipo4_7CV13.sistema_inventario.dto.EstadisticaVentaDTO;
import com.tienda_Equipo4_7CV13.sistema_inventario.dto.ProductoDTO;
import com.tienda_Equipo4_7CV13.sistema_inventario.dto.VentaDTO;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ProductoRepository;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.VentaRepository;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
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
            logger.info("Obteniendo total de productos...");
            Long total = productoRepository.count();
            logger.info("Total de productos: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al obtener total de productos: ", e);
            return 0L;
        }
    }
    
    public Long getTotalClientes() {
        try {
            logger.info("Obteniendo total de clientes...");
            Long total = clienteRepository.count();
            logger.info("Total de clientes: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error al obtener total de clientes: ", e);
            return 0L;
        }
    }
    
    public Long getVentasHoy() {
        try {
            logger.info("Obteniendo ventas de hoy...");
            Long total = ventaRepository.countVentasDelDia();
            logger.info("Ventas de hoy: {}", total);
            return total != null ? total : 0L;
        } catch (Exception e) {
            logger.error("Error al obtener ventas de hoy: ", e);
            return 0L;
        }
    }
    
    public BigDecimal getIngresosHoy() {
        try {
            logger.info("Obteniendo ingresos de hoy...");
            BigDecimal total = ventaRepository.getIngresosDelDia();
            logger.info("Ingresos de hoy: {}", total);
            return total != null ? total : BigDecimal.ZERO;
        } catch (Exception e) {
            logger.error("Error al obtener ingresos de hoy: ", e);
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getIngresosMes() {
        try {
            logger.info("Obteniendo ingresos del mes...");
            BigDecimal total = ventaRepository.getIngresosDelMes();
            logger.info("Ingresos del mes: {}", total);
            return total != null ? total : BigDecimal.ZERO;
        } catch (Exception e) {
            logger.error("Error al obtener ingresos del mes: ", e);
            return BigDecimal.ZERO;
        }
    }
    
    public List<ProductoDTO> getProductosStockBajo() {
        try {
            logger.info("Obteniendo productos con stock bajo...");
            List<ProductoDTO> productos = new ArrayList<>();
            // Por ahora retornamos lista vacía para evitar errores
            logger.info("Productos con stock bajo: {}", productos.size());
            return productos;
        } catch (Exception e) {
            logger.error("Error al obtener productos con stock bajo: ", e);
            return new ArrayList<>();
        }
    }
    
    public List<VentaDTO> getVentasRecientes() {
        try {
            logger.info("Obteniendo ventas recientes...");
            List<VentaDTO> ventas = new ArrayList<>();
            // Por ahora retornamos lista vacía para evitar errores
            logger.info("Ventas recientes: {}", ventas.size());
            return ventas;
        } catch (Exception e) {
            logger.error("Error al obtener ventas recientes: ", e);
            return new ArrayList<>();
        }
    }
    
    public List<EstadisticaVentaDTO> getVentasPorDia() {
        try {
            logger.info("Obteniendo ventas por día...");
            List<EstadisticaVentaDTO> ventas = new ArrayList<>();
            // Por ahora retornamos lista vacía para evitar errores
            logger.info("Ventas por día: {}", ventas.size());
            return ventas;
        } catch (Exception e) {
            logger.error("Error al obtener ventas por día: ", e);
            return new ArrayList<>();
        }
    }
    
    public DashboardDTO getDashboardData() {
        try {
            logger.info("Construyendo datos del dashboard...");
            DashboardDTO dashboard = new DashboardDTO();
            dashboard.setTotalProductos(getTotalProductos());
            dashboard.setTotalClientes(getTotalClientes());
            dashboard.setVentasHoy(getVentasHoy());
            dashboard.setIngresosHoy(getIngresosHoy());
            dashboard.setIngresosMes(getIngresosMes());
            dashboard.setProductosStockBajo(getProductosStockBajo());
            dashboard.setVentasRecientes(getVentasRecientes());
            dashboard.setVentasPorDia(getVentasPorDia());
            logger.info("Dashboard construido exitosamente");
            return dashboard;
        } catch (Exception e) {
            logger.error("Error al construir datos del dashboard: ", e);
            throw new RuntimeException("Error al cargar datos del dashboard", e);
        }
    }
}
