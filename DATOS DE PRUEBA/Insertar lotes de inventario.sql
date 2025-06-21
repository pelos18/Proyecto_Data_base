-- Insertar lotes de inventario
-- Coca-Cola 350ml
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(1, 3, 120, 800.00, 1200.00, SYSDATE-30, SYSDATE+180);

INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(1, 3, 100, 850.00, 1250.00, SYSDATE-10, SYSDATE+200);

-- Pepsi 350ml
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(2, 3, 80, 750.00, 1150.00, SYSDATE-25, SYSDATE+170);

-- Agua Cristal 500ml
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(3, 1, 200, 300.00, 500.00, SYSDATE-15, SYSDATE+365);

-- Leche Alpina 1L
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(4, 5, 50, 2500.00, 3500.00, SYSDATE-5, SYSDATE+15);

INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(4, 5, 40, 2600.00, 3600.00, SYSDATE-2, SYSDATE+20);

-- Yogurt Alpina Fresa
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(5, 5, 60, 1200.00, 1800.00, SYSDATE-3, SYSDATE+12);

-- Queso Colanta
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(6, 5, 25, 8000.00, 12000.00, SYSDATE-7, SYSDATE+30);

-- Jamón Zenú
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(7, 6, 30, 6500.00, 9500.00, SYSDATE-4, SYSDATE+25);

-- Pan Bimbo
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(8, 2, 40, 2200.00, 3200.00, SYSDATE-1, SYSDATE+7);

-- Detergente Ariel
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(9, 4, 20, 8500.00, 12500.00, SYSDATE-20, NULL);

-- Limpiador Fabuloso
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(10, 4, 25, 3500.00, 5200.00, SYSDATE-18, NULL);

-- Arroz Premium
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(11, 1, 50, 2800.00, 4200.00, SYSDATE-12, SYSDATE+730);

-- Aceite Girasol
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_ingreso, fecha_caducidad) VALUES
(12, 1, 35, 4500.00, 6800.00, SYSDATE-8, SYSDATE+540);

COMMIT;

-- Verificar lotes insertados
SELECT 
    l.id_lote,
    p.nombre as producto,
    pr.nombre as proveedor,
    l.cantidad,
    l.precio_compra,
    l.precio_venta,
    TO_CHAR(l.fecha_caducidad, 'DD/MM/YYYY') as caducidad
FROM LOTES_INVENTARIO l
JOIN PRODUCTOS p ON l.id_producto = p.id_producto
JOIN PROVEEDORES pr ON l.id_proveedor = pr.id_proveedor
ORDER BY l.id_lote;