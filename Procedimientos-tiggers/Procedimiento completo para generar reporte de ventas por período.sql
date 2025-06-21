-- Procedimiento completo para generar reporte de ventas por período
CREATE OR REPLACE PROCEDURE sp_reporte_ventas(
    p_fecha_inicio IN DATE,
    p_fecha_fin IN DATE,
    p_id_usuario IN NUMBER DEFAULT NULL,
    p_cursor_ventas OUT SYS_REFCURSOR,
    p_cursor_productos OUT SYS_REFCURSOR,
    p_total_ventas OUT NUMBER,
    p_cantidad_ventas OUT NUMBER,
    p_promedio_venta OUT NUMBER
) AS
BEGIN
    -- Validar fechas
    IF p_fecha_inicio > p_fecha_fin THEN
        RAISE_APPLICATION_ERROR(-20103, 'La fecha de inicio no puede ser mayor a la fecha fin');
    END IF;
    
    -- Calcular totales generales
    SELECT 
        COALESCE(SUM(total), 0),
        COUNT(*),
        CASE 
            WHEN COUNT(*) > 0 THEN COALESCE(AVG(total), 0)
            ELSE 0
        END
    INTO p_total_ventas, p_cantidad_ventas, p_promedio_venta
    FROM VENTAS
    WHERE fecha_venta BETWEEN p_fecha_inicio AND p_fecha_fin
    AND estado = 'COMPLETADA'
    AND (p_id_usuario IS NULL OR id_usuario = p_id_usuario);
    
    -- Cursor con detalle de ventas
    OPEN p_cursor_ventas FOR
        SELECT 
            v.id_venta,
            TO_CHAR(v.fecha_venta, 'DD/MM/YYYY HH24:MI') as fecha_venta_formato,
            v.fecha_venta,
            COALESCE(c.nombre, 'CLIENTE GENERAL') as cliente,
            COALESCE(c.telefono, 'N/A') as telefono_cliente,
            u.nombre as vendedor,
            u.usuario as login_vendedor,
            v.total,
            v.estado,
            COUNT(dv.id_detalle) as items_vendidos,
            SUM(dv.cantidad) as cantidad_total_productos
        FROM VENTAS v
        LEFT JOIN CLIENTES c ON v.id_cliente = c.id_cliente
        JOIN USUARIOS u ON v.id_usuario = u.id_usuario
        LEFT JOIN DETALLE_VENTAS dv ON v.id_venta = dv.id_venta
        WHERE v.fecha_venta BETWEEN p_fecha_inicio AND p_fecha_fin
        AND v.estado = 'COMPLETADA'
        AND (p_id_usuario IS NULL OR v.id_usuario = p_id_usuario)
        GROUP BY v.id_venta, v.fecha_venta, c.nombre, c.telefono, u.nombre, u.usuario, v.total, v.estado
        ORDER BY v.fecha_venta DESC;
    
    -- Cursor con productos más vendidos en el período
    OPEN p_cursor_productos FOR
        SELECT 
            p.id_producto,
            p.codigo_barras,
            p.nombre as producto,
            cat.nombre as categoria,
            m.nombre as marca,
            SUM(dv.cantidad) as cantidad_vendida,
            COUNT(DISTINCT v.id_venta) as numero_ventas,
            SUM(dv.subtotal) as ingresos_generados,
            ROUND(AVG(dv.precio_unitario), 2) as precio_promedio,
            ROUND(SUM(dv.cantidad) / 
                  GREATEST(1, (p_fecha_fin - p_fecha_inicio + 1)), 2) as promedio_diario
        FROM VENTAS v
        JOIN DETALLE_VENTAS dv ON v.id_venta = dv.id_venta
        JOIN LOTES_INVENTARIO l ON dv.id_lote = l.id_lote
        JOIN PRODUCTOS p ON l.id_producto = p.id_producto
        JOIN CATEGORIAS cat ON p.id_categoria = cat.id_categoria
        JOIN MARCAS m ON p.id_marca = m.id_marca
        WHERE v.fecha_venta BETWEEN p_fecha_inicio AND p_fecha_fin
        AND v.estado = 'COMPLETADA'
        AND (p_id_usuario IS NULL OR v.id_usuario = p_id_usuario)
        GROUP BY p.id_producto, p.codigo_barras, p.nombre, cat.nombre, m.nombre
        ORDER BY cantidad_vendida DESC, ingresos_generados DESC;
        
    -- Log del reporte generado
    DBMS_OUTPUT.PUT_LINE('=== REPORTE DE VENTAS GENERADO ===');
    DBMS_OUTPUT.PUT_LINE('Período: ' || TO_CHAR(p_fecha_inicio, 'DD/MM/YYYY') || 
                        ' al ' || TO_CHAR(p_fecha_fin, 'DD/MM/YYYY'));
    DBMS_OUTPUT.PUT_LINE('Usuario: ' || COALESCE(TO_CHAR(p_id_usuario), 'TODOS'));
    DBMS_OUTPUT.PUT_LINE('Total ventas: $' || TO_CHAR(p_total_ventas, '999,999,990.00'));
    DBMS_OUTPUT.PUT_LINE('Cantidad ventas: ' || p_cantidad_ventas);
    DBMS_OUTPUT.PUT_LINE('Promedio por venta: $' || TO_CHAR(p_promedio_venta, '999,999,990.00'));
    DBMS_OUTPUT.PUT_LINE('=====================================');
    
EXCEPTION
    WHEN OTHERS THEN
        -- Cerrar cursores si están abiertos
        IF p_cursor_ventas%ISOPEN THEN
            CLOSE p_cursor_ventas;
        END IF;
        IF p_cursor_productos%ISOPEN THEN
            CLOSE p_cursor_productos;
        END IF;
        RAISE_APPLICATION_ERROR(-20103, 'Error al generar reporte: ' || SQLERRM);
END;
/