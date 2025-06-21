-- Trigger para restaurar stock cuando se cancela una venta
CREATE OR REPLACE TRIGGER trg_restaurar_stock_cancelacion
    AFTER UPDATE ON VENTAS
    FOR EACH ROW
    WHEN (OLD.estado != 'CANCELADA' AND NEW.estado = 'CANCELADA')
BEGIN
    -- Restaurar stock de todos los productos de la venta cancelada
    UPDATE LOTES_INVENTARIO l
    SET cantidad_disponible = cantidad_disponible + (
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