-- Insertar productos de prueba
INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234567', 'Coca-Cola 350ml', 'Bebida gaseosa sabor cola', 1, 1, 50, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234568', 'Pepsi 350ml', 'Bebida gaseosa sabor cola', 1, 2, 30, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234569', 'Agua Cristal 500ml', 'Agua purificada', 1, 10, 100, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234570', 'Leche Entera Alpina 1L', 'Leche entera pasteurizada', 3, 5, 25, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234571', 'Yogurt Alpina Fresa 200g', 'Yogurt sabor fresa', 3, 5, 20, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234572', 'Queso Colanta 500g', 'Queso campesino', 3, 6, 15, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234573', 'Jamón Zenú 250g', 'Jamón de cerdo', 4, 7, 10, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234574', 'Pan Bimbo Integral', 'Pan de molde integral', 6, 4, 20, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234575', 'Detergente Ariel 1kg', 'Detergente en polvo', 5, 9, 12, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234576', 'Limpiador Fabuloso 1L', 'Limpiador multiusos', 5, 8, 15, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234577', 'Arroz Premium 1kg', 'Arroz de primera calidad', 2, 10, 30, 1);

INSERT INTO PRODUCTOS (codigo_barras, nombre, descripcion, id_categoria, id_marca, stock_minimo, activo) VALUES
('7702001234578', 'Aceite Girasol 1L', 'Aceite de girasol refinado', 2, 10, 20, 1);

COMMIT;

-- Verificar productos insertados
SELECT p.id_producto, p.codigo_barras, p.nombre, c.nombre as categoria, m.nombre as marca, p.stock_minimo
FROM PRODUCTOS p
JOIN CATEGORIAS c ON p.id_categoria = c.id_categoria
JOIN MARCAS m ON p.id_marca = m.id_marca
ORDER BY p.id_producto;