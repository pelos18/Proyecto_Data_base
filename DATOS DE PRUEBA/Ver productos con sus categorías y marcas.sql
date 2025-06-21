-- Ver productos con sus categorías y marcas
SELECT 
    p.id_producto,
    p.codigo_barras,
    p.nombre,
    c.nombre as categoria,
    m.nombre as marca,
    p.stock_minimo,
    p.activo
FROM PRODUCTOS p
JOIN CATEGORIAS c ON p.id_categoria = c.id_categoria
JOIN MARCAS m ON p.id_marca = m.id_marca
ORDER BY p.id_producto;

-- Ver lotes de inventario
SELECT 
    l.id_lote,
    p.nombre as producto,
    pr.nombre as proveedor,
    l.cantidad,
    l.precio_compra,
    l.precio_venta,
    TO_CHAR(l.fecha_ingreso, 'DD/MM/YYYY') as fecha_ingreso,
    TO_CHAR(l.fecha_caducidad, 'DD/MM/YYYY') as fecha_caducidad
FROM LOTES_INVENTARIO l
JOIN PRODUCTOS p ON l.id_producto = p.id_producto
JOIN PROVEEDORES pr ON l.id_proveedor = pr.id_proveedor
ORDER BY l.id_lote;