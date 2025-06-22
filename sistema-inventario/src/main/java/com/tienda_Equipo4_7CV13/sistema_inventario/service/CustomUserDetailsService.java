package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario: " + username);
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuarioAndActivoTrue(username);
        
        if (usuarioOpt.isEmpty()) {
            System.out.println("❌ Usuario no encontrado: " + username);
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        
        Usuario usuario = usuarioOpt.get();
        System.out.println("✅ Usuario encontrado: " + usuario.getUsuario() + " con rol: " + usuario.getRol());
        
        // Convertir el rol a formato Spring Security (ROLE_)
        String rol = "ROLE_" + usuario.getRol().toUpperCase();
        System.out.println("🔐 Rol asignado: " + rol);
        
        return User.builder()
                .username(usuario.getUsuario())
                .password(usuario.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(rol)))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
}
