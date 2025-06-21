CREATE OR REPLACE TRIGGER TRG_APERTURA_CAJA_AUTO
BEFORE INSERT ON VENTAS
FOR EACH ROW
DECLARE
    v_caja_count      NUMBER;
    v_usuario_activo  NUMBER;
BEGIN
    -- Primero, verificar si el usuario que realiza la venta está activo
    SELECT activo INTO v_usuario_activo
    FROM USUARIOS
    WHERE id_usuario = :NEW.id_usuario;

    IF v_usuario_activo = 0 THEN
        RAISE_APPLICATION_ERROR(-20011, 'Error: El usuario ' || :NEW.id_usuario || ' no está activo y no puede realizar ventas.');
    END IF;

    -- Contar si ya existe una caja para el usuario en la fecha actual (truncada a día)
    SELECT COUNT(*)
    INTO v_caja_count
    FROM CAJA
    WHERE id_usuario = :NEW.id_usuario
      AND fecha = TRUNC(:NEW.fecha_venta);

    -- Si no existe (count = 0), crearla con saldo inicial de 0
    IF v_caja_count = 0 THEN
        INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos)
        VALUES (:NEW.id_usuario, TRUNC(:NEW.fecha_venta), 0, 0, 0);
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20012, 'Error: El usuario con ID ' || :NEW.id_usuario || ' no existe.');
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20013, 'Error inesperado al intentar abrir la caja: ' || SQLERRM);
END;
/




















