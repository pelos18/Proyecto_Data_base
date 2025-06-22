package com.tienda_Equipo4_7CV13.sistema_inventario.config;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== SISTEMA DE INVENTARIO INICIADO ===");
        
        // SOLO VERIFICAR CONEXIÓN - NO INSERTAR DATOS
        try (Connection connection = dataSource.getConnection()) {
            logger.info("✅ Conexión a Oracle exitosa");
            logger.info("📊 Base de datos: {}", connection.getMetaData().getDatabaseProductName());
            logger.info("🔗 URL: {}", connection.getMetaData().getURL());
            logger.info("👤 Usuario: {}", connection.getMetaData().getUserName());
        } catch (Exception e) {
            logger.error("❌ Error de conexión: {}", e.getMessage());
            return;
        }
        
        logger.info("=== SISTEMA LISTO PARA USAR ===");
        logger.info("🌐 Accede a la interfaz web: http://localhost:8080/login");
        logger.info("📝 Base de datos Oracle vacía y lista para ser llenada desde la interfaz");
        logger.info("🎯 Funcionalidades disponibles:");
        logger.info("   • Login/Registro de usuarios → Tabla USUARIOS");
        logger.info("   • Gestión de productos → Tabla PRODUCTOS");
        logger.info("   • Gestión de clientes → Tabla CLIENTES");
        logger.info("   • Gestión de proveedores → Tabla PROVEEDORES");
        logger.info("   • Control de inventario → Tabla LOTES_INVENTARIO");
        logger.info("   • Procesamiento de ventas → Tablas VENTAS/DETALLE_VENTAS");
        logger.info("💾 TODO SE GUARDA Y LEE DE ORACLE - NO HAY DATOS HARDCODEADOS");
    }
}
