-- Índices corregidos para Oracle (sin WHERE clause)
-- Solo crear los que no existen

-- Verificar qué índices ya existen
SELECT index_name, table_name, uniqueness
FROM user_indexes 
WHERE index_name LIKE 'IDX_%'
ORDER BY table_name, index_name;

-- Crear índices que no existen (ejecutar solo si no están creados)
BEGIN
    -- Índice para lotes por producto y fecha
    BEGIN
        EXECUTE IMMEDIATE 'CREATE INDEX idx_lotes_prod_fecha_v2 ON LOTES_INVENTARIO(id_producto, fecha_ingreso)';
        DBMS_OUTPUT.PUT_LINE('Índice idx_lotes_prod_fecha_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -955 THEN
                DBMS_OUTPUT.PUT_LINE('Índice idx_lotes_prod_fecha_v2 ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Índice para ventas por fecha y usuario
    BEGIN
        EXECUTE IMMEDIATE 'CREATE INDEX idx_ventas_fecha_usr_v2 ON VENTAS(fecha_venta, id_usuario)';
        DBMS_OUTPUT.PUT_LINE('Índice idx_ventas_fecha_usr_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -955 THEN
                DBMS_OUTPUT.PUT_LINE('Índice idx_ventas_fecha_usr_v2 ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Índice para productos por categoría y marca
    BEGIN
        EXECUTE IMMEDIATE 'CREATE INDEX idx_prod_cat_marca_v2 ON PRODUCTOS(id_categoria, id_marca)';
        DBMS_OUTPUT.PUT_LINE('Índice idx_prod_cat_marca_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -955 THEN
                DBMS_OUTPUT.PUT_LINE('Índice idx_prod_cat_marca_v2 ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Índice para búsquedas de productos (función)
    BEGIN
        EXECUTE IMMEDIATE 'CREATE INDEX idx_prod_nombre_upper_v2 ON PRODUCTOS(UPPER(nombre))';
        DBMS_OUTPUT.PUT_LINE('Índice idx_prod_nombre_upper_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -955 THEN
                DBMS_OUTPUT.PUT_LINE('Índice idx_prod_nombre_upper_v2 ya existe');
            ELSE
                RAISE;
            END IF;
    END;

    -- Índice para búsquedas de clientes (función)
    BEGIN
        EXECUTE IMMEDIATE 'CREATE INDEX idx_cli_nombre_upper_v2 ON CLIENTES(UPPER(nombre))';
        DBMS_OUTPUT.PUT_LINE('Índice idx_cli_nombre_upper_v2 creado');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -955 THEN
                DBMS_OUTPUT.PUT_LINE('Índice idx_cli_nombre_upper_v2 ya existe');
            ELSE
                RAISE;
            END IF;
    END;
END;
/