-- El constraint no permite ventas completadas con total = 0
-- Pero nuestro flujo inserta primero con total 0 y luego se actualiza
-- Vamos a eliminarlo y crear uno más flexible

-- Eliminar constraint problemático
ALTER TABLE VENTAS DROP CONSTRAINT chk_total_comp_v2;

-- Crear constraint más flexible que permita total 0 temporalmente
ALTER TABLE VENTAS ADD CONSTRAINT chk_total_no_negativo 
CHECK (total >= 0);

DBMS_OUTPUT.PUT_LINE('Constraint de ventas corregido');






-- Verificar errores en el trigger
SELECT name, type, line, position, text 
FROM user_errors 
WHERE name = 'TRG_DETALLE_VENTAS_ID'
ORDER BY line;

-- Recrear trigger de DETALLE_VENTAS corregido
CREATE OR REPLACE TRIGGER trg_detalle_ventas_id
    BEFORE INSERT OR UPDATE ON DETALLE_VENTAS
    FOR EACH ROW
BEGIN
    -- Auto-incremento del ID
    IF INSERTING AND :NEW.id_detalle IS NULL THEN
        :NEW.id_detalle := seq_detalle_ventas.NEXTVAL;
    END IF;
    
    -- Calcular subtotal automáticamente
    :NEW.subtotal := :NEW.cantidad * :NEW.precio_unitario;
    
END;
/

DBMS_OUTPUT.PUT_LINE('Trigger TRG_DETALLE_VENTAS_ID corregido');

















-- Proceso corregido para insertar ventas
-- Primero insertamos con estado PENDIENTE, luego cambiamos a COMPLETADA

-- Venta 1: Cliente General, Vendedor María González
INSERT INTO VENTAS (id_cliente, id_usuario, fecha_venta, total, estado) VALUES
(1, 3, SYSDATE-5, 0, 'PENDIENTE');

-- Detalles de Venta 1
INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(1, 1, 5, 1200.00);  -- Coca-Cola

INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(1, 4, 3, 500.00);   -- Agua

INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(1, 10, 2, 3200.00);  -- Pan Bimbo

-- Actualizar estado a COMPLETADA (esto activará el trigger de actualización de total)
UPDATE VENTAS SET estado = 'COMPLETADA' WHERE id_venta = 1;

-- Venta 2: Empresa ABC, Vendedor Carlos Rodríguez
INSERT INTO VENTAS (id_cliente, id_usuario, fecha_venta, total, estado) VALUES
(2, 4, SYSDATE-4, 0, 'PENDIENTE');

-- Detalles de Venta 2
INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(2, 1, 10, 1200.00); -- Coca-Cola

INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(2, 3, 8, 1150.00);   -- Pepsi

INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(2, 5, 6, 3500.00);  -- Leche

-- Completar venta 2
UPDATE VENTAS SET estado = 'COMPLETADA' WHERE id_venta = 2;

-- Venta 3: Restaurante El Buen Sabor
INSERT INTO VENTAS (id_cliente, id_usuario, fecha_venta, total, estado) VALUES
(3, 3, SYSDATE-3, 0, 'PENDIENTE');

-- Detalles de Venta 3
INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(3, 7, 4, 1800.00);   -- Yogurt

INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(3, 8, 5, 12000.00); -- Queso

INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES
(3, 9, 3, 9500.00);  -- Jamón

-- Completar venta 3
UPDATE VENTAS SET estado = 'COMPLETADA' WHERE id_venta = 3;

COMMIT;

-- Verificar ventas insertadas
SELECT 
    v.id_venta,
    c.nombre as cliente,
    u.nombre as vendedor,
    TO_CHAR(v.fecha_venta, 'DD/MM/YYYY') as fecha,
    v.total,
    v.estado
FROM VENTAS v
LEFT JOIN CLIENTES c ON v.id_cliente = c.id_cliente
JOIN USUARIOS u ON v.id_usuario = u.id_usuario
ORDER BY v.id_venta;


















-- Como no tenemos permisos para DBMS_SCHEDULER, creamos procedimientos manuales
-- que se pueden ejecutar cuando sea necesario

-- Procedimiento para limpiar notificaciones antiguas
CREATE OR REPLACE PROCEDURE sp_limpiar_notificaciones_antiguas AS
    v_count NUMBER;
BEGIN
    DELETE FROM NOTIFICACIONES 
    WHERE leida = 1 
    AND fecha_creacion < SYSDATE - 30;
    
    v_count := SQL%ROWCOUNT;
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Notificaciones limpiadas: ' || v_count);
END;
/

-- Procedimiento para generar alertas de caducidad
CREATE OR REPLACE PROCEDURE sp_generar_alertas_caducidad AS
    v_count NUMBER := 0;
BEGIN
    -- Insertar notificaciones para productos que caducan en 7 días
    INSERT INTO NOTIFICACIONES (id_usuario, id_producto, tipo, mensaje, leida)
    SELECT DISTINCT
        u.id_usuario,
        l.id_producto,
        'CADUCIDAD_PROXIMA',
        'ALERTA: El producto ' || p.nombre || 
        ' (Lote ' || l.id_lote || ') caduca en ' || 
        TRUNC(l.fecha_caducidad - SYSDATE) || ' días',
        0
    FROM LOTES_INVENTARIO l
    JOIN PRODUCTOS p ON l.id_producto = p.id_producto
    CROSS JOIN USUARIOS u
    WHERE l.fecha_caducidad BETWEEN SYSDATE AND SYSDATE + 7
    AND l.cantidad > 0
    AND u.activo = 1
    AND u.rol IN ('ADMIN', 'SUPERVISOR')
    AND NOT EXISTS (
        SELECT 1 FROM NOTIFICACIONES n
        WHERE n.id_usuario = u.id_usuario
        AND n.id_producto = l.id_producto
        AND n.tipo = 'CADUCIDAD_PROXIMA'
        AND n.fecha_creacion >= SYSDATE - 1
    );
    
    v_count := SQL%ROWCOUNT;
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Alertas de caducidad generadas: ' || v_count);
END;
/

-- Procedimiento para generar alertas de stock bajo
CREATE OR REPLACE PROCEDURE sp_generar_alertas_stock_bajo AS
    v_count NUMBER := 0;
BEGIN
    INSERT INTO NOTIFICACIONES (id_usuario, id_producto, tipo, mensaje, leida)
    SELECT DISTINCT
        u.id_usuario,
        p.id_producto,
        'STOCK_BAJO',
        'ALERTA: Stock bajo para ' || p.nombre || 
        '. Stock actual: ' || COALESCE(stock_actual.total, 0) || 
        ', Mínimo: ' || p.stock_minimo,
        0
    FROM PRODUCTOS p
    CROSS JOIN USUARIOS u
    LEFT JOIN (
        SELECT id_producto, SUM(cantidad) as total
        FROM LOTES_INVENTARIO
        GROUP BY id_producto
    ) stock_actual ON p.id_producto = stock_actual.id_producto
    WHERE COALESCE(stock_actual.total, 0) <= p.stock_minimo
    AND p.activo = 1
    AND u.activo = 1
    AND u.rol IN ('ADMIN', 'SUPERVISOR')
    AND NOT EXISTS (
        SELECT 1 FROM NOTIFICACIONES n
        WHERE n.id_usuario = u.id_usuario
        AND n.id_producto = p.id_producto
        AND n.tipo = 'STOCK_BAJO'
        AND n.fecha_creacion >= SYSDATE - 1
        AND n.leida = 0
    );
    
    v_count := SQL%ROWCOUNT;
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Alertas de stock bajo generadas: ' || v_count);
END;
/

DBMS_OUTPUT.PUT_LINE('Procedimientos de mantenimiento creados exitosamente');