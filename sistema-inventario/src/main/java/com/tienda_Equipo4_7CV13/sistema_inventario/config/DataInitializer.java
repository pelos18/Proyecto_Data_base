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
        // Crear usuario admin por defecto si no existe
        if (!usuarioRepository.existsByUsuario("admin")) {
            Usuario admin = new Usuario();
            admin.setUsuario("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setRol("ADMIN");
            admin.setActivo(true);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado - Usuario: admin, Contraseña: admin123");
        }
        
        // Crear usuario vendedor por defecto si no existe
        if (!usuarioRepository.existsByUsuario("vendedor")) {
            Usuario vendedor = new Usuario();
            vendedor.setUsuario("vendedor");
            vendedor.setPassword(passwordEncoder.encode("vendedor123"));
            vendedor.setNombre("Vendedor");
            vendedor.setRol("VENDEDOR");
            vendedor.setActivo(true);
            
            usuarioRepository.save(vendedor);
            System.out.println("Usuario vendedor creado - Usuario: vendedor, Contraseña: vendedor123");
        }
    }
}
