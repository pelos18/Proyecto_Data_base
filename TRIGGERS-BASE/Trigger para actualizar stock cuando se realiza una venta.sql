-- Trigger para actualizar stock cuando se realiza una venta
CREATE OR REPLACE TRIGGER trg_actualizar_stock_venta
    AFTER INSERT ON DETALLE_VENTAS
    FOR EACH ROW
DECLARE
    v_stock_disponible NUMBER;
BEGIN
    -- Verificar que hay stock suficiente
    SELECT cantidad_disponible 
    INTO v_stock_disponible
    FROM LOTES_INVENTARIO 
    WHERE id_lote = :NEW.id_lote;
    
    IF v_stock_disponible < :NEW.cantidad THEN
        RAISE_APPLICATION_ERROR(-20001, 
            'Stock insuficiente. Disponible: ' || v_stock_disponible || 
            ', Solicitado: ' || :NEW.cantidad);
    END IF;
    
    -- Actualizar stock disponible
    UPDATE LOTES_INVENTARIO 
    SET cantidad_disponible = cantidad_disponible - :NEW.cantidad
    WHERE id_lote = :NEW.id_lote;
    
    -- Log de la operación
    DBMS_OUTPUT.PUT_LINE('Stock actualizado para lote: ' || :NEW.id_lote || 
                        ', Cantidad vendida: ' || :NEW.cantidad);
END;
/