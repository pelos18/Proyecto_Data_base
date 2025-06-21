CREATE OR REPLACE TRIGGER TRG_VENTA_ANULADA_LOGICA
AFTER UPDATE OF estado ON VENTAS
FOR EACH ROW
-- Se activa si el nuevo estado es DEVUELTA o CANCELADA (y es diferente al anterior)
WHEN ( (NEW.estado = 'DEVUELTA' OR NEW.estado = 'CANCELADA') AND NEW.estado <> OLD.estado )
DECLARE
    -- Cursor para recorrer cada uno de los productos vendidos en esta venta
    CURSOR c_detalle_venta IS
        SELECT id_lote, cantidad
        FROM DETALLE_VENTAS
        WHERE id_venta = :NEW.id_venta;
BEGIN
    -- 1. Devolver los productos al inventario
    FOR item IN c_detalle_venta LOOP
        UPDATE LOTES_INVENTARIO
        SET cantidad = cantidad + item.cantidad
        WHERE id_lote = item.id_lote;
    END LOOP;

    -- 2. Registrar el monto de la venta como un egreso en la caja
    UPDATE CAJA
    SET egresos = egresos + :NEW.total
    WHERE id_usuario = :NEW.id_usuario
      AND fecha = TRUNC(:NEW.fecha_venta);

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20015, 'Error inesperado al procesar la devolución/cancelación: ' || SQLERRM);
END;
/