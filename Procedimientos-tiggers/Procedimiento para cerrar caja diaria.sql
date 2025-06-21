-- Procedimiento para cerrar caja diaria
CREATE OR REPLACE PROCEDURE sp_cerrar_caja(
    p_id_usuario IN NUMBER,
    p_fecha IN DATE DEFAULT SYSDATE,
    p_egresos_adicionales IN NUMBER DEFAULT 0,
    p_observaciones IN VARCHAR2 DEFAULT NULL
) AS
    v_id_caja NUMBER;
    v_ventas_efectivo NUMBER := 0;
    v_saldo_inicial NUMBER;
    v_ingresos_actuales NUMBER;
    v_egresos_totales NUMBER;
    v_saldo_final NUMBER;
BEGIN
    -- Buscar caja del día
    BEGIN
        SELECT id_caja, saldo_inicial, ingresos, egresos
        INTO v_id_caja, v_saldo_inicial, v_ingresos_actuales, v_egresos_totales
        FROM CAJA
        WHERE id_usuario = p_id_usuario
        AND TRUNC(fecha) = TRUNC(p_fecha)
        AND ROWNUM = 1;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- Si no existe caja, crear una
            INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final)
            VALUES (p_id_usuario, TRUNC(p_fecha), 0, 0, p_egresos_adicionales, -p_egresos_adicionales)
            RETURNING id_caja INTO v_id_caja;
            
            DBMS_OUTPUT.PUT_LINE('Caja creada y cerrada para usuario: ' || p_id_usuario);
            RETURN;
    END;
    
    -- Calcular ventas en efectivo del día (si las hubiera en el futuro)
    SELECT COALESCE(SUM(total), 0)
    INTO v_ventas_efectivo
    FROM VENTAS
    WHERE id_usuario = p_id_usuario
    AND TRUNC(fecha_venta) = TRUNC(p_fecha)
    AND estado = 'COMPLETADA';
    
    -- Actualizar egresos si se proporcionaron adicionales
    v_egresos_totales := v_egresos_totales + p_egresos_adicionales;
    
    -- Calcular saldo final
    v_saldo_final := v_saldo_inicial + v_ingresos_actuales - v_egresos_totales;
    
    -- Actualizar caja
    UPDATE CAJA 
    SET egresos = v_egresos_totales,
        saldo_final = v_saldo_final
    WHERE id_caja = v_id_caja;
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Caja cerrada exitosamente:');
    DBMS_OUTPUT.PUT_LINE('- ID Caja: ' || v_id_caja);
    DBMS_OUTPUT.PUT_LINE('- Saldo inicial: ' || v_saldo_inicial);
    DBMS_OUTPUT.PUT_LINE('- Ingresos: ' || v_ingresos_actuales);
    DBMS_OUTPUT.PUT_LINE('- Egresos: ' || v_egresos_totales);
    DBMS_OUTPUT.PUT_LINE('- Saldo final: ' || v_saldo_final);
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20102, 'Error al cerrar caja: ' || SQLERRM);
END;
/