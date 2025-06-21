-- Constraints adicionales con nombres únicos para Oracle
BEGIN
    -- Constraint para cantidad disponible
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE LOTES_INVENTARIO ADD CONSTRAINT chk_cant_disp_v2 CHECK (cantidad_disponible <= cantidad)';
        DBMS_OUTPUT.PUT_LINE('Constraint chk_cant_disp_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2264 OR SQLCODE = -2275 THEN
                DBMS_OUTPUT.PUT_LINE('Constraint para cantidad disponible ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Constraint para precio de venta mayor que compra
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE LOTES_INVENTARIO ADD CONSTRAINT chk_precio_venta_v2 CHECK (precio_venta >= precio_compra)';
        DBMS_OUTPUT.PUT_LINE('Constraint chk_precio_venta_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2264 OR SQLCODE = -2275 THEN
                DBMS_OUTPUT.PUT_LINE('Constraint para precio de venta ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Constraint para total de ventas completadas
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE VENTAS ADD CONSTRAINT chk_total_comp_v2 CHECK (estado != ''COMPLETADA'' OR total > 0)';
        DBMS_OUTPUT.PUT_LINE('Constraint chk_total_comp_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2264 OR SQLCODE = -2275 THEN
                DBMS_OUTPUT.PUT_LINE('Constraint para total completada ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Constraint para subtotal positivo
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE DETALLE_VENTAS ADD CONSTRAINT chk_subtotal_pos_v2 CHECK (subtotal >= 0)';
        DBMS_OUTPUT.PUT_LINE('Constraint chk_subtotal_pos_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2264 OR SQLCODE = -2275 THEN
                DBMS_OUTPUT.PUT_LINE('Constraint para subtotal positivo ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Constraint para longitud de usuario
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE USUARIOS ADD CONSTRAINT chk_usuario_long_v2 CHECK (LENGTH(usuario) >= 3)';
        DBMS_OUTPUT.PUT_LINE('Constraint chk_usuario_long_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2264 OR SQLCODE = -2275 THEN
                DBMS_OUTPUT.PUT_LINE('Constraint para longitud usuario ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Constraint para stock mínimo positivo
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE PRODUCTOS ADD CONSTRAINT chk_stock_min_pos_v2 CHECK (stock_minimo >= 0)';
        DBMS_OUTPUT.PUT_LINE('Constraint chk_stock_min_pos_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2264 OR SQLCODE = -2275 THEN
                DBMS_OUTPUT.PUT_LINE('Constraint para stock mínimo ya existe');
            ELSE
                RAISE;
            END IF;
    END;
END;
/