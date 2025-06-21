-- 1. Realizar la venta
INSERT INTO VENTAS (id_cliente, id_usuario, estado) VALUES (NULL, 2, 'PENDIENTE');
INSERT INTO DETALLE_VENTAS (id_venta, id_lote, cantidad, precio_unitario) VALUES (1, 1, 2, 25.00);
COMMIT;

-- 2. Aceptar el pago
UPDATE VENTAS SET estado = 'ACEPTADA' WHERE id_venta = 1;
COMMIT;

-- 3. VERIFICAR: Revisa la caja del vendedor. INGRESOS debe ser 50.
SELECT * FROM CAJA WHERE id_usuario = 2;
-- VERIFICAR: Revisa el stock de leche. Debe ser 98.
SELECT cantidad FROM LOTES_INVENTARIO WHERE id_producto = 1;







