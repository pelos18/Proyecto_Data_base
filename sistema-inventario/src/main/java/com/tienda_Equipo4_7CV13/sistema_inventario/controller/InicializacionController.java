package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inicializar")
public class InicializacionController {

    @Autowired
    private DataSource dataSource;

    @PostMapping("/datos-prueba")
    public ResponseEntity<Map<String, Object>> crearDatosPrueba() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Verificar si ya existen datos
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM CATEGORIAS");
            rs.next();
            if (rs.getInt(1) > 0) {
                response.put("success", false);
                response.put("message", "Ya existen datos en el sistema");
                return ResponseEntity.ok(response);
            }
            
            // Crear datos de prueba
            String[] queries = {
                "INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Bebidas', 'Refrescos, jugos y bebidas')",
                "INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Snacks', 'Botanas y dulces')",
                "INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Lácteos', 'Leche, quesos y yogurt')",
                "INSERT INTO MARCAS (nombre, descripcion) VALUES ('Coca-Cola', 'Bebidas refrescantes')",
                "INSERT INTO MARCAS (nombre, descripcion) VALUES ('Sabritas', 'Botanas y snacks')",
                "INSERT INTO PROVEEDORES (nombre, contacto, telefono) VALUES ('Distribuidora Central', 'Juan Pérez', '555-0001')",
                "INSERT INTO CLIENTES (nombre, telefono) VALUES ('Cliente General', '555-1001')"
            };
            
            for (String query : queries) {
                stmt.executeUpdate(query);
            }
            
            conn.commit();
            
            response.put("success", true);
            response.put("message", "Datos de prueba creados exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
