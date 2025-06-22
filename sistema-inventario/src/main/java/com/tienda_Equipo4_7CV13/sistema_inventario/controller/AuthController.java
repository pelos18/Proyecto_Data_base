package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Cliente;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.UsuarioRepository;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // ===== REGISTRO DE EMPLEADOS =====
    @GetMapping("/registro-empleado")
    public String registroEmpleadoPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro-empleado";
    }

    @PostMapping("/registro-empleado")
    public String registrarEmpleado(@RequestParam String usuario,
                                  @RequestParam String password,
                                  @RequestParam String nombre,
                                  @RequestParam String rol,
                                  RedirectAttributes redirectAttributes) {
        try {
            logger.info("=== REGISTRANDO EMPLEADO ===");
            logger.info("Usuario: {}, Nombre: {}, Rol: {}", usuario, nombre, rol);

            // Validaciones
            if (usuario == null || usuario.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El usuario es requerido");
                return "redirect:/registro-empleado";
            }

            if (password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "La contraseña es requerida");
                return "redirect:/registro-empleado";
            }

            if (nombre == null || nombre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre es requerido");
                return "redirect:/registro-empleado";
            }

            if (!rol.equals("dueño") && !rol.equals("administrativo") && !rol.equals("vendedor")) {
                redirectAttributes.addFlashAttribute("error", "Rol inválido");
                return "redirect:/registro-empleado";
            }

            if (usuarioRepository.existsByUsuario(usuario)) {
                redirectAttributes.addFlashAttribute("error", "El usuario ya existe");
                return "redirect:/registro-empleado";
            }

            // Crear empleado
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsuario(usuario.trim());
            nuevoUsuario.setNombre(nombre.trim());
            nuevoUsuario.setRol(rol.trim());
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setPassword(passwordEncoder.encode(password));

            usuarioRepository.save(nuevoUsuario);
            logger.info("Empleado registrado exitosamente");

            redirectAttributes.addFlashAttribute("success", "Empleado registrado exitosamente");
            return "redirect:/login?success";

        } catch (Exception e) {
            logger.error("Error al registrar empleado: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al registrar empleado: " + e.getMessage());
            return "redirect:/registro-empleado";
        }
    }

    // ===== REGISTRO DE CLIENTES =====
    @GetMapping("/registro-cliente")
    public String registroClientePage(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "auth/registro-cliente";
    }

    @PostMapping("/registro-cliente")
    public String registrarCliente(@RequestParam String nombre,
                                 @RequestParam(required = false) String telefono,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) String direccion,
                                 RedirectAttributes redirectAttributes) {
        try {
            logger.info("=== REGISTRANDO CLIENTE ===");
            logger.info("Nombre: {}, Teléfono: {}, Email: {}", nombre, telefono, email);

            // Validación básica
            if (nombre == null || nombre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre es requerido");
                return "redirect:/registro-cliente";
            }

            // Verificar si el email ya existe (si se proporciona)
            if (email != null && !email.trim().isEmpty() && clienteRepository.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un cliente con ese email");
                return "redirect:/registro-cliente";
            }

            // Crear cliente
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(nombre.trim());
            nuevoCliente.setTelefono(telefono != null ? telefono.trim() : null);
            nuevoCliente.setEmail(email != null ? email.trim() : null);
            nuevoCliente.setDireccion(direccion != null ? direccion.trim() : null);

            clienteRepository.save(nuevoCliente);
            logger.info("Cliente registrado exitosamente");

            redirectAttributes.addFlashAttribute("success", 
                "¡Te has registrado exitosamente como cliente! Tu información ha sido guardada.");
            return "redirect:/registro-cliente";

        } catch (Exception e) {
            logger.error("Error al registrar cliente: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al registrar cliente: " + e.getMessage());
            return "redirect:/registro-cliente";
        }
    }

    // ===== DASHBOARD =====
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsuario(username).orElse(null);
        
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            
            switch (usuario.getRol().toLowerCase()) {
                case "vendedor":
                    return "dashboard/cajero";
                case "administrativo":
                    return "dashboard/admin";
                case "dueño":
                    return "dashboard/admin";
                default:
                    return "dashboard/index";
            }
        }
        
        return "dashboard/index";
    }

    // ===== REGISTRO LEGACY (mantener por compatibilidad) =====
    @GetMapping("/registro")
    public String registroLegacy() {
        return "redirect:/registro-empleado";
    }
}
