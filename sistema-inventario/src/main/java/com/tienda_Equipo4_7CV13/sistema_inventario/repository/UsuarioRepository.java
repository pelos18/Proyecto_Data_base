package com.tienda_Equipo4_7CV13.sistema_inventario.repository;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByUsuario(String usuario);
    
    // Buscar usuario por nombre de usuario y que esté activo
    Optional<Usuario> findByUsuarioAndActivoTrue(String usuario);
    
    // Buscar usuarios por rol
    List<Usuario> findByRol(String rol);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Buscar usuarios por rol y activos
    List<Usuario> findByRolAndActivoTrue(String rol);
    
    // Verificar si existe un usuario con ese nombre
    boolean existsByUsuario(String usuario);
    
    // Buscar usuarios por nombre (contiene)
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Contar usuarios por rol
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    Long countByRolAndActivoTrue(@Param("rol") String rol);
    
    // Obtener usuarios con ventas en un período
    @Query("SELECT DISTINCT u FROM Usuario u JOIN Venta v ON u.idUsuario = v.usuario.idUsuario " +
           "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<Usuario> findUsuariosConVentasEnPeriodo(@Param("fechaInicio") java.time.LocalDate fechaInicio, 
                                                @Param("fechaFin") java.time.LocalDate fechaFin);
}
