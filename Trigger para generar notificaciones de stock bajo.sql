-- Trigger para generar notificaciones de stock bajo
CREATE OR REPLACE TRIGGER trg_notificacion_stock_bajo
    AFTER UPDATE ON LOTES_INVENTARIO
    FOR EACH ROW
DECLARE
    v_stock_total NUMBER;
    v_stock_minimo NUMBER;
    v_nombre_producto VARCHAR2(100);
    v_count_notif NUMBER;
BEGIN
    -- Solo procesar si cambió la cantidad disponible
    IF :OLD.cantidad_disponible != :NEW.cantidad_disponible THEN
        
        -- Obtener stock total del producto y stock mínimo
        SELECT 
            SUM(l.cantidad_disponible),
            p.stock_minimo,
            p.nombre
        INTO v_stock_total, v_stock_minimo, v_nombre_producto
        FROM LOTES_INVENTARIO l
        JOIN PRODUCTOS p ON l.id_producto = p.id_producto
        WHERE l.id_producto = :NEW.id_producto
        AND l.activo = 1
        GROUP BY p.stock_minimo, p.nombre;
        
        -- Si el stock total está por debajo del mínimo
        IF v_stock_total <= v_stock_minimo THEN
            
            -- Verificar si ya existe una notificación reciente (últimas 24 horas)
            SELECT COUNT(*)
            INTO v_count_notif
            FROM NOTIFICACIONES
            WHERE id_producto = :NEW.id_producto
            AND tipo = 'STOCK_BAJO'
            AND fecha_creacion >= SYSDATE - 1
            AND leida = 0;
            
            -- Solo crear notificación si no existe una reciente
            IF v_count_notif = 0 THEN
                -- Crear notificación para todos los usuarios activos
                INSERT INTO NOTIFICACIONES (id_usuario, id_producto, tipo, mensaje, prioridad)
                SELECT 
                    u.id_usuario,
                    :NEW.id_producto,
                    'STOCK_BAJO',
                    'ALERTA: Stock bajo para ' || v_nombre_producto || 
                    '. Stock actual: ' || v_stock_total || 
                    ', Mínimo requerido: ' || v_stock_minimo,
                    CASE 
                        WHEN v_stock_total = 0 THEN 'CRITICA'
                        WHEN v_stock_total <= (v_stock_minimo * 0.5) THEN 'ALTA'
                        ELSE 'MEDIA'
                    END
                FROM USUARIOS u
                WHERE u.activo = 1
                AND u.rol IN ('ADMIN', 'SUPERVISOR');
            END IF;
        END IF;
    END IF;
END;
/