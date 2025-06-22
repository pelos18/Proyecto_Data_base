package com.tienda_Equipo4_7CV13.sistema_inventario.config;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO DATAINITIALIZER ===");
        
        try {
            // Crear usuario admin por defecto si no existe (usando rol 'dueño')
            if (!usuarioRepository.existsByUsuario("admin")) {
                System.out.println("Creando usuario admin...");
                Usuario admin = new Usuario();
                admin.setUsuario("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setNombre("Administrador");
                admin.setRol("dueño"); // Cambiado de ADMIN a dueño
                admin.setActivo(true);
                
                usuarioRepository.save(admin);
                System.out.println("✅ Usuario admin creado - Usuario: admin, Contraseña: admin");
            } else {
                System.out.println("⚠️ Usuario admin ya existe");
            }
            
            // Crear usuario vendedor por defecto si no existe
            if (!usuarioRepository.existsByUsuario("vendedor2")) {
                System.out.println("Creando usuario vendedor2...");
                Usuario vendedor = new Usuario();
                vendedor.setUsuario("vendedor2");
                vendedor.setPassword(passwordEncoder.encode("vendedor"));
                vendedor.setNombre("Vendedor Dos");
                vendedor.setRol("vendedor"); // Cambiado de VENDEDOR a vendedor
                vendedor.setActivo(true);
                
                usuarioRepository.save(vendedor);
                System.out.println("✅ Usuario vendedor2 creado - Usuario: vendedor2, Contraseña: vendedor");
            } else {
                System.out.println("⚠️ Usuario vendedor2 ya existe");
            }
            
            System.out.println("=== DATAINITIALIZER COMPLETADO ===");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR EN DATAINITIALIZER: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
