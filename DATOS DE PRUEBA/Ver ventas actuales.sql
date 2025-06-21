-- Ver ventas actuales
SELECT 
    v.id_venta,
    COALESCE(c.nombre, 'Sin cliente') as cliente,
    u.nombre as vendedor,
    TO_CHAR(v.fecha_venta, 'DD/MM/YYYY') as fecha,
    v.total,
    v.estado
FROM VENTAS v
LEFT JOIN CLIENTES c ON v.id_cliente = c.id_cliente
JOIN USUARIOS u ON v.id_usuario = u.id_usuario
ORDER BY v.id_venta;

-- Ver detalles de ventas
SELECT 
    dv.id_detalle,
    dv.id_venta,
    p.nombre as producto,
    dv.cantidad,
    dv.precio_unitario,
    dv.subtotal
FROM DETALLE_VENTAS dv
JOIN LOTES_INVENTARIO l ON dv.id_lote = l.id_lote
JOIN PRODUCTOS p ON l.id_producto = p.id_producto
ORDER BY dv.id_venta, dv.id_detalle;