package com.tienda_Equipo4_7CV13.sistema_inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.tienda_Equipo4_7CV13.sistema_inventario.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    
    // Esta clase habilita los repositorios JPA y el manejo de transacciones
    // La configuración de conexión está en application.properties
}
