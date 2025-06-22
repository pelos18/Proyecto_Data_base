package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.UsuarioException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    // Obtener usuario por ID
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioException.UsuarioNotFoundException(id));
    }
    
    // Obtener usuario por nombre de usuario
    public Usuario findByUsuario(String usuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get();
        }
        throw new RuntimeException("Usuario no encontrado: " + usuario);
    }
    
    // Obtener usuario activo por nombre de usuario
    public Usuario findByUsuarioActivo(String usuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuarioAndActivoTrue(usuario);
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get();
        }
        throw new RuntimeException("Usuario no encontrado o inactivo: " + usuario);
    }
    
    // Crear nuevo usuario
    public Usuario save(Usuario usuario) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.existsByUsuario(usuario.getUsuario())) {
            throw new UsuarioException.UsuarioYaExisteException(usuario.getUsuario());
        }
        
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        return usuarioRepository.save(usuario);
    }
    
    // Actualizar usuario
    public Usuario update(Usuario usuario) {
        Usuario usuarioExistente = findById(usuario.getIdUsuario());
        
        // Verificar si el nuevo nombre de usuario ya existe (si cambió)
        if (!usuarioExistente.getUsuario().equals(usuario.getUsuario()) && 
            usuarioRepository.existsByUsuario(usuario.getUsuario())) {
            throw new UsuarioException.UsuarioYaExisteException(usuario.getUsuario());
        }
        
        // Si se proporciona nueva contraseña, encriptarla
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            // Mantener la contraseña existente
            usuario.setPassword(usuarioExistente.getPassword());
        }
        
        return usuarioRepository.save(usuario);
    }
    
    // Eliminar usuario (desactivar)
    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
    
    // Activar usuario
    public Usuario activar(Long id) {
        Usuario usuario = findById(id);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
    
    // Obtener usuarios por rol
    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRolAndActivoTrue(rol);
    }
    
    // Obtener usuarios activos
    public List<Usuario> findUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }
    
    // Buscar usuarios por nombre
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Cambiar contraseña
    public void cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        Usuario usuario = findById(id);
        
        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new UsuarioException.CredencialesInvalidasException();
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
    }
    
    // Validar credenciales
    public boolean validarCredenciales(String usuario, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuarioAndActivoTrue(usuario);
        if (usuarioOpt.isPresent()) {
            return passwordEncoder.matches(password, usuarioOpt.get().getPassword());
        }
        return false;
    }
    
    // Contar usuarios por rol
    public Long contarPorRol(String rol) {
        return usuarioRepository.countByRolAndActivoTrue(rol);
    }
}
