package com.tienda_Equipo4_7CV13.sistema_inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    
    // Esta configuración habilita la auditoría automática de JPA
    // Para campos como fechaCreacion, fechaModificacion, etc.
}
