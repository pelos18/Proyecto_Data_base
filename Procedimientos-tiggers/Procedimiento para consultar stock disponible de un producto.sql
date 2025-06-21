-- Procedimiento para consultar stock disponible de un producto
CREATE OR REPLACE PROCEDURE sp_consultar_stock(
    p_id_producto IN NUMBER,
    p_stock_total OUT NUMBER,
    p_stock_minimo OUT NUMBER,
    p_estado_stock OUT VARCHAR2,
    p_lotes_disponibles OUT SYS_REFCURSOR
) AS
BEGIN
    -- Obtener stock total y mínimo
    SELECT 
        COALESCE(SUM(l.cantidad), 0),
        p.stock_minimo
    INTO p_stock_total, p_stock_minimo
    FROM PRODUCTOS p
    LEFT JOIN LOTES_INVENTARIO l ON p.id_producto = l.id_producto
    WHERE p.id_producto = p_id_producto
    GROUP BY p.stock_minimo;
    
    -- Determinar estado del stock
    IF p_stock_total = 0 THEN
        p_estado_stock := 'SIN_STOCK';
    ELSIF p_stock_total <= p_stock_minimo THEN
        p_estado_stock := 'STOCK_BAJO';
    ELSE
        p_estado_stock := 'NORMAL';
    END IF;
    
    -- Obtener lotes disponibles
    OPEN p_lotes_disponibles FOR
        SELECT 
            l.id_lote,
            l.cantidad,
            l.precio_venta,
            l.fecha_ingreso,
            l.fecha_caducidad,
            pr.nombre as proveedor,
            CASE 
                WHEN l.fecha_caducidad <= SYSDATE THEN 'CADUCADO'
                WHEN l.fecha_caducidad <= SYSDATE + 30 THEN 'PROXIMO_CADUCAR'
                ELSE 'VIGENTE'
            END as estado_caducidad
        FROM LOTES_INVENTARIO l
        JOIN PROVEEDORES pr ON l.id_proveedor = pr.id_proveedor
        WHERE l.id_producto = p_id_producto
        AND l.cantidad > 0
        ORDER BY l.fecha_caducidad ASC, l.fecha_ingreso ASC;
        
    DBMS_OUTPUT.PUT_LINE('Stock consultado - Producto: ' || p_id_producto || 
                        ', Stock total: ' || p_stock_total || 
                        ', Estado: ' || p_estado_stock);
                        
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_stock_total := 0;
        p_stock_minimo := 0;
        p_estado_stock := 'PRODUCTO_NO_ENCONTRADO';
        OPEN p_lotes_disponibles FOR SELECT NULL FROM DUAL WHERE 1=0;
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20101, 'Error al consultar stock: ' || SQLERRM);
END;
/