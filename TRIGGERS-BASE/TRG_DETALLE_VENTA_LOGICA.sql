CREATE OR REPLACE TRIGGER TRG_DETALLE_VENTA_LOGICA
FOR INSERT OR DELETE OR UPDATE ON DETALLE_VENTAS
COMPOUND TRIGGER
    -- Colección para guardar los IDs de las ventas afectadas
    TYPE T_VENTA_ID IS TABLE OF VENTAS.id_venta%TYPE INDEX BY PLS_INTEGER;
    v_id_ventas_afectadas T_VENTA_ID;

    -- Se ejecuta una vez por cada fila afectada
    AFTER EACH ROW IS
        v_cantidad_restante NUMBER;
    BEGIN
        -- Guardamos el ID de la venta afectada para usarla después
        v_id_ventas_afectadas(NVL(:NEW.id_venta, :OLD.id_venta)) := NVL(:NEW.id_venta, :OLD.id_venta);

        IF INSERTING THEN
            -- Descontar stock del lote
            UPDATE LOTES_INVENTARIO SET cantidad = cantidad - :NEW.cantidad WHERE id_lote = :NEW.id_lote;
            -- Verificar que no quede stock negativo
            SELECT cantidad INTO v_cantidad_restante FROM LOTES_INVENTARIO WHERE id_lote = :NEW.id_lote;
            IF v_cantidad_restante < 0 THEN
                RAISE_APPLICATION_ERROR(-20001, 'Stock insuficiente o negativo.');
            END IF;
        ELSIF DELETING THEN
            -- Devolver stock al lote
            UPDATE LOTES_INVENTARIO SET cantidad = cantidad + :OLD.cantidad WHERE id_lote = :OLD.id_lote;
        ELSIF UPDATING ('cantidad') THEN
            -- Ajustar stock por la diferencia
            UPDATE LOTES_INVENTARIO SET cantidad = cantidad + (:OLD.cantidad - :NEW.cantidad) WHERE id_lote = :NEW.id_lote;
            -- Verificar que no quede stock negativo
            SELECT cantidad INTO v_cantidad_restante FROM LOTES_INVENTARIO WHERE id_lote = :NEW.id_lote;
            IF v_cantidad_restante < 0 THEN
                RAISE_APPLICATION_ERROR(-20002, 'La actualización resultaría en stock negativo.');
            END IF;
        END IF;
    END AFTER EACH ROW;

    -- Se ejecuta una sola vez al final de toda la operación (INSERT/DELETE/UPDATE)
    AFTER STATEMENT IS
        v_key NUMBER; -- Variable para almacenar la clave de la colección
    BEGIN
        -- === INICIO DE LA CORRECCIÓN ===
        -- Recorremos la colección de ventas afectadas de forma segura con un bucle WHILE
        v_key := v_id_ventas_afectadas.FIRST;
        WHILE v_key IS NOT NULL LOOP
            -- Recalculamos el total de la venta sumando los (cantidad * precio) de sus detalles
            UPDATE VENTAS v
            SET v.total = (SELECT NVL(SUM(d.cantidad * d.precio_unitario), 0)
                           FROM DETALLE_VENTAS d
                           WHERE d.id_venta = v_id_ventas_afectadas(v_key))
            WHERE v.id_venta = v_id_ventas_afectadas(v_key);

            -- Pasamos a la siguiente venta afectada en la colección
            v_key := v_id_ventas_afectadas.NEXT(v_key);
        END LOOP;
        -- === FIN DE LA CORRECCIÓN ===
    END AFTER STATEMENT;

END TRG_DETALLE_VENTA_LOGICA;
/











