CREATE OR REPLACE TRIGGER TRG_LOTE_NOTIF_STOCK_BAJO
FOR UPDATE OF cantidad ON LOTES_INVENTARIO
COMPOUND TRIGGER
    -- Usamos una colección para guardar los IDs de los productos afectados
    TYPE T_PRODUCTO_ID_TABLE IS TABLE OF LOTES_INVENTARIO.id_producto%TYPE INDEX BY PLS_INTEGER;
    g_affected_products T_PRODUCTO_ID_TABLE;

    -- Se ejecuta por cada fila que cambia. Solo guarda el ID del producto.
    AFTER EACH ROW IS
    BEGIN
        -- Si el stock se redujo, guardamos el ID del producto para revisarlo después
        IF :NEW.cantidad < :OLD.cantidad THEN
            g_affected_products(:NEW.id_producto) := :NEW.id_producto;
        END IF;
    END AFTER EACH ROW;

    -- Se ejecuta al final de la sentencia, cuando la tabla ya no está "mutando"
    AFTER STATEMENT IS
        v_stock_actual NUMBER;
        v_stock_minimo PRODUCTOS.stock_minimo%TYPE;
        v_producto_id  PRODUCTOS.id_producto%TYPE;
    BEGIN
        -- Recorremos la lista de productos afectados que guardamos
        v_producto_id := g_affected_products.FIRST;
        WHILE v_producto_id IS NOT NULL LOOP
            -- Ahora SÍ es seguro leer la tabla LOTES_INVENTARIO
            SELECT NVL(SUM(li.cantidad), 0) INTO v_stock_actual
            FROM LOTES_INVENTARIO li
            WHERE li.id_producto = v_producto_id;

            -- Obtenemos el stock mínimo para comparar
            SELECT p.stock_minimo INTO v_stock_minimo
            FROM PRODUCTOS p
            WHERE p.id_producto = v_producto_id;

            -- Si el stock actual es menor o igual al mínimo, insertamos la notificación
            IF v_stock_actual <= v_stock_minimo THEN
                INSERT INTO NOTIFICACIONES (id_usuario, id_producto, tipo, mensaje, fecha_creacion)
                VALUES (1, v_producto_id, 'Stock Bajo',
                        'Stock bajo para producto ' || v_producto_id || ' (Stock actual: ' || v_stock_actual || ')', SYSDATE);
            END IF;

            -- Pasamos al siguiente producto en la lista
            v_producto_id := g_affected_products.NEXT(v_producto_id);
        END LOOP;
    END AFTER STATEMENT;
END TRG_LOTE_NOTIF_STOCK_BAJO;
/