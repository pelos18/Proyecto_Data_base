-- Trigger para actualizar automáticamente el total de la venta
CREATE OR REPLACE TRIGGER trg_actualizar_total_venta
    AFTER INSERT OR UPDATE OR DELETE ON DETALLE_VENTAS
    FOR EACH ROW
DECLARE
    v_id_venta NUMBER;
    v_nuevo_total NUMBER;
BEGIN
    -- Determinar el ID de venta afectado
    IF DELETING THEN
        v_id_venta := :OLD.id_venta;
    ELSE
        v_id_venta := :NEW.id_venta;
    END IF;
    
    -- Calcular nuevo total
    SELECT COALESCE(SUM(subtotal), 0)
    INTO v_nuevo_total
    FROM DETALLE_VENTAS
    WHERE id_venta = v_id_venta;
    
    -- Actualizar el total en la venta
    UPDATE VENTAS 
    SET total = v_nuevo_total - descuento + impuesto,
        fecha_modificacion = SYSDATE
    WHERE id_venta = v_id_venta;
    
    DBMS_OUTPUT.PUT_LINE('Total actualizado para venta: ' || v_id_venta || 
                        ', Nuevo total: ' || v_nuevo_total);
END;
/

-- Agregar columna fecha_modificacion si no existe
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE VENTAS ADD fecha_modificacion DATE DEFAULT SYSDATE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1430 THEN -- Column already exists
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/