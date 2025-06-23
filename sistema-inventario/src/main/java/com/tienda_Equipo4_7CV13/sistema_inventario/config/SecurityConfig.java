package com.tienda_Equipo4_7CV13.sistema_inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // PÁGINAS PÚBLICAS
                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                // AGREGAR ESTAS LÍNEAS:
                .requestMatchers("/registro-empleado", "/registro-cliente").permitAll()
                .requestMatchers("/catalogo-cliente/**").permitAll()
                .requestMatchers("/test-empleado", "/test-cliente").permitAll()
                
                // TODOS LOS ROLES PUEDEN VER PRODUCTOS Y CLIENTES
                .requestMatchers("/api/productos/**").hasAnyRole("VENDEDOR", "ADMINISTRATIVO", "DUEÑO")
                .requestMatchers("/api/clientes/**").hasAnyRole("VENDEDOR", "ADMINISTRATIVO", "DUEÑO")
                .requestMatchers("/api/categorias/**").hasAnyRole("VENDEDOR", "ADMINISTRATIVO", "DUEÑO")
                .requestMatchers("/api/marcas/**").hasAnyRole("VENDEDOR", "ADMINISTRATIVO", "DUEÑO")
                
                // VENTAS - TODOS PUEDEN PROCESAR
                .requestMatchers("/api/ventas/**").hasAnyRole("VENDEDOR", "ADMINISTRATIVO", "DUEÑO")
                
                // ADMINISTRACIÓN - SOLO ADMIN Y DUEÑO
                .requestMatchers("/api/admin/**").hasAnyRole("ADMINISTRATIVO", "DUEÑO")
                .requestMatchers("/api/inventario/**").hasAnyRole("ADMINISTRATIVO", "DUEÑO")
                .requestMatchers("/api/proveedores/**").hasAnyRole("ADMINISTRATIVO", "DUEÑO")
                
                // DASHBOARDS POR ROL
                .requestMatchers("/dashboard/**").hasAnyRole("VENDEDOR", "ADMINISTRATIVO", "DUEÑO")
                
                // RESTO REQUIERE AUTENTICACIÓN
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
