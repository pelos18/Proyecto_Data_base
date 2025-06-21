-- Triggers para generar IDs
CREATE OR REPLACE TRIGGER TRG_USUARIOS_BI
BEFORE INSERT ON USUARIOS
FOR EACH ROW
BEGIN
    SELECT SEQ_USUARIOS.NEXTVAL INTO :NEW.id_usuario FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_CLIENTES_BI
BEFORE INSERT ON CLIENTES
FOR EACH ROW
BEGIN
    SELECT SEQ_CLIENTES.NEXTVAL INTO :NEW.id_cliente FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_PROVEEDORES_BI
BEFORE INSERT ON PROVEEDORES
FOR EACH ROW
BEGIN
    SELECT SEQ_PROVEEDORES.NEXTVAL INTO :NEW.id_proveedor FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_CATEGORIAS_BI
BEFORE INSERT ON CATEGORIAS
FOR EACH ROW
BEGIN
    SELECT SEQ_CATEGORIAS.NEXTVAL INTO :NEW.id_categoria FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_MARCAS_BI
BEFORE INSERT ON MARCAS
FOR EACH ROW
BEGIN
    SELECT SEQ_MARCAS.NEXTVAL INTO :NEW.id_marca FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_PRODUCTOS_BI
BEFORE INSERT ON PRODUCTOS
FOR EACH ROW
BEGIN
    SELECT SEQ_PRODUCTOS.NEXTVAL INTO :NEW.id_producto FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_LOTES_INVENTARIO_BI
BEFORE INSERT ON LOTES_INVENTARIO
FOR EACH ROW
BEGIN
    SELECT SEQ_LOTES_INVENTARIO.NEXTVAL INTO :NEW.id_lote FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_VENTAS_BI
BEFORE INSERT ON VENTAS
FOR EACH ROW
BEGIN
    SELECT SEQ_VENTAS.NEXTVAL INTO :NEW.id_venta FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_CAJA_BI
BEFORE INSERT ON CAJA
FOR EACH ROW
BEGIN
    SELECT SEQ_CAJA.NEXTVAL INTO :NEW.id_caja FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_NOTIFICACIONES_BI
BEFORE INSERT ON NOTIFICACIONES
FOR EACH ROW
BEGIN
    SELECT SEQ_NOTIFICACIONES.NEXTVAL INTO :NEW.id_notificacion FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_DETALLE_VENTAS_BI
BEFORE INSERT ON DETALLE_VENTAS
FOR EACH ROW
BEGIN
    SELECT SEQ_DETALLE_VENTAS.NEXTVAL INTO :NEW.id_detalle FROM DUAL;
END;
/

-- Trigger para lógica de DETALLE_VENTAS y cálculo de total
CREATE OR REPLACE TRIGGER TRG_DETALLE_VENTA_LOGICA
FOR INSERT OR DELETE OR UPDATE ON DETALLE_VENTAS
COMPOUND TRIGGER
    TYPE T_VENTA_ID IS TABLE OF VENTAS.id_venta%TYPE INDEX BY PLS_INTEGER;
    v_id_ventas_afectadas T_VENTA_ID;
    v_cantidad_restante NUMBER;

    AFTER EACH ROW IS
    BEGIN
        v_id_ventas_afectadas(NVL(:NEW.id_venta, :OLD.id_venta)) := NVL(:NEW.id_venta, :OLD.id_venta);

        IF INSERTING THEN
            UPDATE LOTES_INVENTARIO
            SET cantidad = cantidad - :NEW.cantidad
            WHERE id_lote = :NEW.id_lote;
            SELECT cantidad INTO v_cantidad_restante
            FROM LOTES_INVENTARIO
            WHERE id_lote = :NEW.id_lote;
            IF SQL%ROWCOUNT = 0 OR v_cantidad_restante < 0 THEN
                RAISE_APPLICATION_ERROR(-20001, 'Stock insuficiente o negativo.');
            END IF;
        ELSIF DELETING THEN
            UPDATE LOTES_INVENTARIO
            SET cantidad = cantidad + :OLD.cantidad
            WHERE id_lote = :OLD.id_lote;
        ELSIF UPDATING ('cantidad') THEN
            UPDATE LOTES_INVENTARIO
            SET cantidad = cantidad + (:OLD.cantidad - :NEW.cantidad)
            WHERE id_lote = :NEW.id_lote;
            SELECT cantidad INTO v_cantidad_restante
            FROM LOTES_INVENTARIO
            WHERE id_lote = :NEW.id_lote;
            IF v_cantidad_restante < 0 THEN
                RAISE_APPLICATION_ERROR(-20002, 'La actualización resultaría en stock negativo.');
            END IF;
        END IF;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
    BEGIN
        FOR v_key IN v_id_ventas_afectadas.FIRST .. v_id_ventas_afectadas.LAST LOOP
            UPDATE VENTAS v
            SET v.total = (SELECT NVL(SUM(d.cantidad * d.precio_unitario), 0)
                          FROM DETALLE_VENTAS d
                          WHERE d.id_venta = v_id_ventas_afectadas(v_key))
            WHERE v.id_venta = v_id_ventas_afectadas(v_key);
        END LOOP;
    END AFTER STATEMENT;

END TRG_DETALLE_VENTA_LOGICA;
/

-- Trigger para restringir modificación de precios a dueño y administrativo
CREATE OR REPLACE TRIGGER TRG_RESTRICT_PRECIOS
BEFORE INSERT OR UPDATE OF precio_compra, precio_venta ON LOTES_INVENTARIO
FOR EACH ROW
DECLARE
    v_rol USUARIOS.rol%TYPE;
BEGIN
    -- Obtener el rol del usuario que realiza la operación (simulado con contexto)
    -- Nota: Esto requiere integración con la sesión de Spring Boot. Por ahora, usa un valor fijo como ejemplo
    SELECT rol INTO v_rol
    FROM USUARIOS
    WHERE id_usuario = SYS_CONTEXT('USERENV', 'CLIENT_IDENTIFIER'); -- Ajustar con lógica de sesión

    IF v_rol NOT IN ('dueño', 'administrativo') THEN
        RAISE_APPLICATION_ERROR(-20003, 'Solo los roles "dueño" y "administrativo" pueden modificar precios.');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20004, 'Usuario no encontrado.');
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20005, 'Error al verificar permisos: ' || SQLERRM);
END;
/

-- Trigger para notificaciones de stock bajo
CREATE OR REPLACE TRIGGER TRG_LOTE_NOTIF_STOCK_BAJO
AFTER UPDATE OF cantidad ON LOTES_INVENTARIO
FOR EACH ROW
DECLARE
    v_stock_actual NUMBER;
    v_stock_minimo PRODUCTOS.stock_minimo%TYPE;
BEGIN
    IF :NEW.cantidad < :OLD.cantidad THEN
        SELECT NVL(SUM(li.cantidad), 0) INTO v_stock_actual
        FROM LOTES_INVENTARIO li
        WHERE li.id_producto = :NEW.id_producto;
        SELECT p.stock_minimo INTO v_stock_minimo
        FROM PRODUCTOS p
        WHERE p.id_producto = :NEW.id_producto;
        IF v_stock_actual <= v_stock_minimo THEN
            INSERT INTO NOTIFICACIONES (id_notificacion, id_usuario, id_producto, tipo, mensaje, fecha_creacion)
            VALUES (SEQ_NOTIFICACIONES.NEXTVAL, 1, :NEW.id_producto, 'Stock Bajo',
                    'Stock bajo para producto ' || :NEW.id_producto || ' (Stock: ' || v_stock_actual || ')', SYSDATE);
        END IF;
    END IF;
END;
/

-- Trigger para actualizar stock_actual en PRODUCTOS
CREATE OR REPLACE TRIGGER TRG_UPDATE_STOCK_ACTUAL
FOR INSERT OR UPDATE OR DELETE ON LOTES_INVENTARIO
COMPOUND TRIGGER
    TYPE t_id_producto_tab IS TABLE OF LOTES_INVENTARIO.id_producto%TYPE;
    l_id_productos t_id_producto_tab := t_id_producto_tab();

    AFTER EACH ROW IS
    BEGIN
        IF INSERTING OR UPDATING OR DELETING THEN
            l_id_productos.EXTEND;
            l_id_productos(l_id_productos.LAST) := NVL(:NEW.id_producto, :OLD.id_producto);
        END IF;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
    BEGIN
        FOR i IN 1 .. l_id_productos.COUNT LOOP
            UPDATE PRODUCTOS p
            SET p.stock_actual = (SELECT NVL(SUM(li.cantidad), 0)
                                FROM LOTES_INVENTARIO li
                                WHERE li.id_producto = l_id_productos(i))
            WHERE p.id_producto = l_id_productos(i);
        END LOOP;
        l_id_productos.DELETE;
    END AFTER STATEMENT;

END TRG_UPDATE_STOCK_ACTUAL;
/

-- Trigger para caducidad de lotes
CREATE OR REPLACE TRIGGER TRG_LOTE_CADUCIDAD
AFTER INSERT OR UPDATE OF fecha_caducidad ON LOTES_INVENTARIO
FOR EACH ROW
DECLARE
    v_dias_restantes NUMBER;
BEGIN
    IF :NEW.fecha_caducidad IS NOT NULL THEN
        v_dias_restantes := :NEW.fecha_caducidad - TRUNC(SYSDATE);
        IF v_dias_restantes <= 30 AND v_dias_restantes > 0 THEN
            INSERT INTO NOTIFICACIONES (id_notificacion, id_usuario, id_producto, tipo, mensaje, fecha_creacion)
            VALUES (SEQ_NOTIFICACIONES.NEXTVAL, 1, :NEW.id_producto, 'Caducidad Próxima',
                    'Lote ' || :NEW.id_lote || ' caduca en ' || v_dias_restantes || ' días.', SYSDATE);
        END IF;
    END IF;
END;
/