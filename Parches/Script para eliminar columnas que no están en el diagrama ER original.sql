-- Script para eliminar columnas que no están en el diagrama ER original
BEGIN
    -- Eliminar columnas extra de USUARIOS si existen
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE USUARIOS DROP COLUMN fecha_creacion';
        DBMS_OUTPUT.PUT_LINE('Columna fecha_creacion eliminada de USUARIOS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE USUARIOS DROP COLUMN fecha_modificacion';
        DBMS_OUTPUT.PUT_LINE('Columna fecha_modificacion eliminada de USUARIOS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    -- Eliminar columnas extra de CLIENTES si existen
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE CLIENTES DROP COLUMN fecha_registro';
        DBMS_OUTPUT.PUT_LINE('Columna fecha_registro eliminada de CLIENTES');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE CLIENTES DROP COLUMN activo';
        DBMS_OUTPUT.PUT_LINE('Columna activo eliminada de CLIENTES');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    -- Eliminar columnas extra de PROVEEDORES si existen
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE PROVEEDORES DROP COLUMN email';
        DBMS_OUTPUT.PUT_LINE('Columna email eliminada de PROVEEDORES');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE PROVEEDORES DROP COLUMN contacto';
        DBMS_OUTPUT.PUT_LINE('Columna contacto eliminada de PROVEEDORES');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE PROVEEDORES DROP COLUMN fecha_registro';
        DBMS_OUTPUT.PUT_LINE('Columna fecha_registro eliminada de PROVEEDORES');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE PROVEEDORES DROP COLUMN activo';
        DBMS_OUTPUT.PUT_LINE('Columna activo eliminada de PROVEEDORES');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    -- Eliminar columnas extra de LOTES_INVENTARIO si existen
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE LOTES_INVENTARIO DROP COLUMN cantidad_disponible';
        DBMS_OUTPUT.PUT_LINE('Columna cantidad_disponible eliminada de LOTES_INVENTARIO');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE LOTES_INVENTARIO DROP COLUMN lote_proveedor';
        DBMS_OUTPUT.PUT_LINE('Columna lote_proveedor eliminada de LOTES_INVENTARIO');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE LOTES_INVENTARIO DROP COLUMN activo';
        DBMS_OUTPUT.PUT_LINE('Columna activo eliminada de LOTES_INVENTARIO');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    -- Eliminar columnas extra de VENTAS si existen
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE VENTAS DROP COLUMN tipo_pago';
        DBMS_OUTPUT.PUT_LINE('Columna tipo_pago eliminada de VENTAS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE VENTAS DROP COLUMN descuento';
        DBMS_OUTPUT.PUT_LINE('Columna descuento eliminada de VENTAS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE VENTAS DROP COLUMN impuesto';
        DBMS_OUTPUT.PUT_LINE('Columna impuesto eliminada de VENTAS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE VENTAS DROP COLUMN observaciones';
        DBMS_OUTPUT.PUT_LINE('Columna observaciones eliminada de VENTAS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    -- Eliminar columnas extra de DETALLE_VENTAS si existen
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE DETALLE_VENTAS DROP COLUMN descuento_item';
        DBMS_OUTPUT.PUT_LINE('Columna descuento_item eliminada de DETALLE_VENTAS');
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;

    DBMS_OUTPUT.PUT_LINE('Limpieza de columnas extra completada');
END;
/