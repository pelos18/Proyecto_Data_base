package com.tienda_Equipo4_7CV13.sistema_inventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class SistemaInventarioApplicationTests {

    @Test
    void contextLoads() {
        // Test básico para verificar que el contexto de Spring carga correctamente
        // Sin cargar el contexto web completo para evitar problemas de configuración
    }
}
