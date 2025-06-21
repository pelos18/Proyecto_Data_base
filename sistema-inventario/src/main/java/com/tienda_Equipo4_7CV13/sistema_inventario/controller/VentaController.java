package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Venta;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.VentaService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @GetMapping
    public ResponseEntity<List<Venta>> getAllVentas() {
        try {
            List<Venta> ventas = ventaService.findAll();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Long id) {
        try {
            Venta venta = ventaService.findById(id);
            return venta != null ? ResponseEntity.ok(venta) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Venta> createVenta(@Valid @RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.save(venta);
            return ResponseEntity.ok(nuevaVenta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Long id, @Valid @RequestBody Venta venta) {
        try {
            venta.setIdVenta(id);
            Venta ventaActualizada = ventaService.save(venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/hoy")
    public ResponseEntity<List<Venta>> getVentasHoy() {
        try {
            List<Venta> ventas = ventaService.findVentasByFecha(LocalDate.now());
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/por-fecha")
    public ResponseEntity<List<Venta>> getVentasPorFecha(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            List<Venta> ventas = ventaService.findVentasByRangoFecha(inicio, fin);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Venta> confirmarVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.confirmarVenta(id);
            return ResponseEntity.ok(venta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Venta> cancelarVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.cancelarVenta(id);
            return ResponseEntity.ok(venta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
