-- Procedimiento para realizar una venta completa
CREATE OR REPLACE PROCEDURE sp_realizar_venta(
    p_id_cliente IN NUMBER,
    p_id_usuario IN NUMBER,
    p_productos IN VARCHAR2, -- JSON-like: 'id_lote:cantidad:precio,id_lote:cantidad:precio'
    p_estado IN VARCHAR2 DEFAULT 'COMPLETADA',
    p_id_venta OUT NUMBER
) AS
    v_total NUMBER := 0;
    v_producto VARCHAR2(100);
    v_id_lote NUMBER;
    v_cantidad NUMBER;
    v_precio NUMBER;
    v_subtotal NUMBER;
    v_pos_inicio NUMBER := 1;
    v_pos_fin NUMBER;
    v_pos_campo NUMBER;
BEGIN
    -- Crear la venta
    INSERT INTO VENTAS (id_cliente, id_usuario, fecha_venta, total, estado)
    VALUES (p_id_cliente, p_id_usuario, SYSDATE, 0, p_estado)
    RETURNING id_venta INTO p_id_venta;
    
    -- Procesar cada producto (formato: id_lote:cantidad:precio,id_lote:cantidad:precio)
    WHILE v_pos_inicio <= LENGTH(p_productos) LOOP
        -- Encontrar el siguiente producto
        v_pos_fin := INSTR(p_productos, ',', v_pos_inicio);
        IF v_pos_fin = 0 THEN
            v_pos_fin := LENGTH(p_productos) + 1;
        END IF;
        
        v_producto := SUBSTR(p_productos, v_pos_inicio, v_pos_fin - v_pos_inicio);
        
        -- Extraer id_lote
        v_pos_campo := INSTR(v_producto, ':');
        v_id_lote := TO_NUMBER(SUBSTR(v_producto, 1, v_pos_campo - 1));
        
        -- Extraer cantidad
        v_producto := SUBSTR(v_producto, v_pos_campo + 1);
        v_pos_campo := INSTR(v_producto, ':');
        v_cantidad := TO_NUMBER(SUBSTR(v_producto, 1, v_pos_campo - 1));
        
        -- Extraer precio
        v_precio := TO_NUMBER(SUBSTR(v_producto, v_pos_campo + 1));
        
        -- Calcular subtotal
        v_subtotal := v_cantidad * v_precio;
        
        -- Insertar detalle de venta
        INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario, subtotal)
        VALUES (p_id_venta, v_id_lote, v_cantidad, v_precio, v_subtotal);
        
        v_total := v_total + v_subtotal;
        
        -- Siguiente producto
        v_pos_inicio := v_pos_fin + 1;
    END LOOP;
    
    -- Actualizar total de la venta
    UPDATE VENTAS SET total = v_total WHERE id_venta = p_id_venta;
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Venta realizada exitosamente. ID: ' || p_id_venta || ', Total: ' || v_total);
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20100, 'Error al realizar venta: ' || SQLERRM);
END;
/