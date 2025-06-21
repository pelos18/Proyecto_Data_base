-- Actualizar trigger de stock que puede estar fallando por columna eliminada
CREATE OR REPLACE TRIGGER trg_actualizar_stock_venta
    AFTER INSERT ON DETALLE_VENTAS
    FOR EACH ROW
DECLARE
    v_cantidad_actual NUMBER;
BEGIN
    -- Verificar cantidad actual en el lote
    SELECT cantidad 
    INTO v_cantidad_actual
    FROM LOTES_INVENTARIO 
    WHERE id_lote = :NEW.id_lote;
    
    IF v_cantidad_actual < :NEW.cantidad THEN
        RAISE_APPLICATION_ERROR(-20001, 
            'Stock insuficiente. Disponible: ' || v_cantidad_actual || 
            ', Solicitado: ' || :NEW.cantidad);
    END IF;
    
    -- Actualizar cantidad en lote (reducir stock)
    UPDATE LOTES_INVENTARIO 
    SET cantidad = cantidad - :NEW.cantidad
    WHERE id_lote = :NEW.id_lote;
    
    DBMS_OUTPUT.PUT_LINE('Stock actualizado para lote: ' || :NEW.id_lote || 
                        ', Cantidad vendida: ' || :NEW.cantidad);
END;
/

-- Actualizar trigger de restaurar stock para cancelaciones
CREATE OR REPLACE TRIGGER trg_restaurar_stock_cancelacion
    AFTER UPDATE ON VENTAS
    FOR EACH ROW
    WHEN (OLD.estado != 'CANCELADA' AND NEW.estado = 'CANCELADA')
BEGIN
    -- Restaurar stock de todos los productos de la venta cancelada
    UPDATE LOTES_INVENTARIO l
    SET cantidad = cantidad + (
        SELECT dv.cantidad 
        FROM DETALLE_VENTAS dv 
        WHERE dv.id_venta = :NEW.id_venta 
        AND dv.id_lote = l.id_lote
    )
    WHERE l.id_lote IN (
        SELECT dv.id_lote 
        FROM DETALLE_VENTAS dv 
        WHERE dv.id_venta = :NEW.id_venta
    );
    
    DBMS_OUTPUT.PUT_LINE('Stock restaurado para venta cancelada: ' || :NEW.id_venta);
END;
/