package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasAnyRole('DUEÑO', 'ADMINISTRATIVO')")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarUsuarios(Model model) {
        try {
            model.addAttribute("usuarios", usuarioRepository.findAll());
            return "usuarios/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar usuarios: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/detalle/{id}")
    public String detalleUsuario(@PathVariable Long id, Model model) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            model.addAttribute("usuario", usuario);
            return "usuarios/detalle";
        } catch (Exception e) {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/activar/{id}")
    public String activarUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
            return "redirect:/usuarios?success=Usuario activado exitosamente";
        } catch (Exception e) {
            return "redirect:/usuarios?error=Error al activar usuario";
        }
    }

    @PostMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            return "redirect:/usuarios?success=Usuario desactivado exitosamente";
        } catch (Exception e) {
            return "redirect:/usuarios?error=Error al desactivar usuario";
        }
    }
}
