package com.tienda_Equipo4_7CV13.sistema_inventario.config;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== SISTEMA DE INVENTARIO INICIADO ===");
        
        // Solo crear un usuario admin inicial si no existe
        if (!usuarioRepository.existsByUsuario("admin")) {
            Usuario admin = new Usuario();
            admin.setUsuario("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setNombre("Administrador");
            admin.setRol("dueño");
            admin.setActivo(true);
            usuarioRepository.save(admin);
            logger.info("✅ Usuario admin inicial creado - Usuario: admin, Contraseña: admin");
        }
        
        logger.info("=== SISTEMA LISTO PARA USAR ===");
        logger.info("🌐 Accede a la interfaz web: http://localhost:8080/api/login");
        logger.info("👤 Usuario inicial: admin / admin");
        logger.info("📝 Usa la interfaz para gestionar todos los datos");
    }
}
