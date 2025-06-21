CREATE OR REPLACE TRIGGER TRG_VENTA_ACEPTADA_CAJA
AFTER UPDATE OF estado ON VENTAS
FOR EACH ROW
WHEN (NEW.estado = 'ACEPTADA' AND OLD.estado <> 'ACEPTADA')
BEGIN
    -- Actualiza la caja del usuario en la fecha de la venta, sumando el total a los ingresos.
    -- El trigger anterior (TRG_APERTURA_CAJA_AUTO) ya se aseguró de que la caja exista.
    UPDATE CAJA
    SET ingresos = ingresos + :NEW.total
    WHERE id_usuario = :NEW.id_usuario
      AND fecha = TRUNC(:NEW.fecha_venta);
END;
/