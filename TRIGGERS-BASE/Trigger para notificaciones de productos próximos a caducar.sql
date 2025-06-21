-- Trigger para notificaciones de productos próximos a caducar
CREATE OR REPLACE TRIGGER trg_notificacion_caducidad
    BEFORE INSERT OR UPDATE ON LOTES_INVENTARIO
    FOR EACH ROW
DECLARE
    v_dias_caducidad NUMBER;
    v_nombre_producto VARCHAR2(100);
    v_count_notif NUMBER;
BEGIN
    -- Solo procesar si hay fecha de caducidad y cantidad disponible
    IF :NEW.fecha_caducidad IS NOT NULL AND :NEW.cantidad_disponible > 0 THEN
        
        v_dias_caducidad := TRUNC(:NEW.fecha_caducidad - SYSDATE);
        
        -- Obtener nombre del producto
        SELECT nombre INTO v_nombre_producto
        FROM PRODUCTOS 
        WHERE id_producto = :NEW.id_producto;
        
        -- Si está próximo a caducar (30 días o menos)
        IF v_dias_caducidad <= 30 THEN
            
            -- Verificar si ya existe notificación para este lote
            SELECT COUNT(*)
            INTO v_count_notif
            FROM NOTIFICACIONES
            WHERE mensaje LIKE '%Lote ' || :NEW.id_lote || '%'
            AND tipo IN ('CADUCIDAD_PROXIMA', 'PRODUCTO_CADUCADO')
            AND leida = 0;
            
            IF v_count_notif = 0 THEN
                INSERT INTO NOTIFICACIONES (id_usuario, id_producto, tipo, mensaje, prioridad)
                SELECT 
                    u.id_usuario,
                    :NEW.id_producto,
                    CASE 
                        WHEN v_dias_caducidad <= 0 THEN 'PRODUCTO_CADUCADO'
                        ELSE 'CADUCIDAD_PROXIMA'
                    END,
                    CASE 
                        WHEN v_dias_caducidad <= 0 THEN 
                            'URGENTE: Producto CADUCADO - ' || v_nombre_producto || 
                            ' (Lote ' || :NEW.id_lote || '). Caducó hace ' || ABS(v_dias_caducidad) || ' días'
                        WHEN v_dias_caducidad <= 7 THEN
                            'CRÍTICO: Producto caduca en ' || v_dias_caducidad || ' días - ' || 
                            v_nombre_producto || ' (Lote ' || :NEW.id_lote || ')'
                        ELSE
                            'AVISO: Producto caduca en ' || v_dias_caducidad || ' días - ' || 
                            v_nombre_producto || ' (Lote ' || :NEW.id_lote || ')'
                    END,
                    CASE 
                        WHEN v_dias_caducidad <= 0 THEN 'CRITICA'
                        WHEN v_dias_caducidad <= 7 THEN 'ALTA'
                        WHEN v_dias_caducidad <= 15 THEN 'MEDIA'
                        ELSE 'BAJA'
                    END
                FROM USUARIOS u
                WHERE u.activo = 1
                AND u.rol IN ('ADMIN', 'SUPERVISOR', 'VENDEDOR');
            END IF;
        END IF;
    END IF;
END;
/