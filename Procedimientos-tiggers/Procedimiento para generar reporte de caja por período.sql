-- Procedimiento para generar reporte de caja por período
CREATE OR REPLACE PROCEDURE sp_reporte_caja(
    p_fecha_inicio IN DATE,
    p_fecha_fin IN DATE,
    p_id_usuario IN NUMBER DEFAULT NULL,
    p_cursor_caja OUT SYS_REFCURSOR,
    p_total_ingresos OUT NUMBER,
    p_total_egresos OUT NUMBER,
    p_saldo_neto OUT NUMBER
) AS
BEGIN
    -- Calcular totales
    SELECT 
        COALESCE(SUM(ingresos), 0),
        COALESCE(SUM(egresos), 0),
        COALESCE(SUM(saldo_final), 0)
    INTO p_total_ingresos, p_total_egresos, p_saldo_neto
    FROM CAJA
    WHERE fecha BETWEEN p_fecha_inicio AND p_fecha_fin
    AND (p_id_usuario IS NULL OR id_usuario = p_id_usuario);
    
    -- Abrir cursor con detalle de caja
    OPEN p_cursor_caja FOR
        SELECT 
            c.id_caja,
            TO_CHAR(c.fecha, 'DD/MM/YYYY') as fecha_formato,
            c.fecha,
            u.nombre as cajero,
            u.usuario as login_cajero,
            c.saldo_inicial,
            c.ingresos,
            c.egresos,
            c.saldo_final,
            -- Calcular diferencia con ventas del día
            COALESCE(v.total_ventas, 0) as ventas_registradas,
            (c.ingresos - COALESCE(v.total_ventas, 0)) as diferencia_caja,
            CASE 
                WHEN ABS(c.ingresos - COALESCE(v.total_ventas, 0)) <= 1 THEN 'CUADRADA'
                WHEN (c.ingresos - COALESCE(v.total_ventas, 0)) > 1 THEN 'SOBRANTE'
                ELSE 'FALTANTE'
            END as estado_cuadre
        FROM CAJA c
        JOIN USUARIOS u ON c.id_usuario = u.id_usuario
        LEFT JOIN (
            SELECT 
                TRUNC(fecha_venta) as fecha,
                id_usuario,
                SUM(total) as total_ventas
            FROM VENTAS
            WHERE estado = 'COMPLETADA'
            GROUP BY TRUNC(fecha_venta), id_usuario
        ) v ON TRUNC(c.fecha) = v.fecha AND c.id_usuario = v.id_usuario
        WHERE c.fecha BETWEEN p_fecha_inicio AND p_fecha_fin
        AND (p_id_usuario IS NULL OR c.id_usuario = p_id_usuario)
        ORDER BY c.fecha DESC;
        
    DBMS_OUTPUT.PUT_LINE('=== REPORTE DE CAJA GENERADO ===');
    DBMS_OUTPUT.PUT_LINE('Período: ' || TO_CHAR(p_fecha_inicio, 'DD/MM/YYYY') || 
                        ' al ' || TO_CHAR(p_fecha_fin, 'DD/MM/YYYY'));
    DBMS_OUTPUT.PUT_LINE('Total ingresos: $' || TO_CHAR(p_total_ingresos, '999,999,990.00'));
    DBMS_OUTPUT.PUT_LINE('Total egresos: $' || TO_CHAR(p_total_egresos, '999,999,990.00'));
    DBMS_OUTPUT.PUT_LINE('Saldo neto: $' || TO_CHAR(p_saldo_neto, '999,999,990.00'));
    DBMS_OUTPUT.PUT_LINE('===============================');
    
EXCEPTION
    WHEN OTHERS THEN
        IF p_cursor_caja%ISOPEN THEN
            CLOSE p_cursor_caja;
        END IF;
        RAISE_APPLICATION_ERROR(-20104, 'Error al generar reporte de caja: ' || SQLERRM);
END;
/