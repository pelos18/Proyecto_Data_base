-- Trigger para actualizar caja cuando se completa una venta (solo con columnas del esquema original)
CREATE OR REPLACE TRIGGER trg_actualizar_caja_venta
    AFTER UPDATE ON VENTAS
    FOR EACH ROW
    WHEN (OLD.estado != 'COMPLETADA' AND NEW.estado = 'COMPLETADA')
DECLARE
    v_id_caja NUMBER;
BEGIN
    -- Buscar si hay una caja abierta para el usuario en la fecha actual
    BEGIN
        SELECT id_caja
        INTO v_id_caja
        FROM CAJA
        WHERE id_usuario = :NEW.id_usuario
        AND TRUNC(fecha) = TRUNC(SYSDATE)
        AND ROWNUM = 1;
        
        -- Actualizar ingresos de la caja existente
        UPDATE CAJA
        SET ingresos = ingresos + :NEW.total,
            saldo_final = saldo_inicial + (ingresos + :NEW.total) - egresos
        WHERE id_caja = v_id_caja;
        
        DBMS_OUTPUT.PUT_LINE('Caja actualizada. Venta: ' || :NEW.id_venta || 
                            ', Monto: ' || :NEW.total);
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- Si no hay caja para hoy, crear una nueva
            INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final)
            VALUES (:NEW.id_usuario, TRUNC(SYSDATE), 0, :NEW.total, 0, :NEW.total);
            
            DBMS_OUTPUT.PUT_LINE('Nueva caja creada para usuario: ' || :NEW.id_usuario || 
                                ', Venta: ' || :NEW.id_venta);
    END;
END;
/