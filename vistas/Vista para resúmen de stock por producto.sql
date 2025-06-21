-- Vista para resúmen de stock por producto
CREATE OR REPLACE VIEW VISTA_STOCK_PRODUCTOS AS
SELECT 
    p.id_producto,
    p.nombre,
    p.stock_minimo,
    NVL(SUM(li.cantidad), 0) AS stock_actual,
    p.activo
FROM PRODUCTOS p
LEFT JOIN LOTES_INVENTARIO li ON p.id_producto = li.id_producto
GROUP BY p.id_producto, p.nombre, p.stock_minimo, p.activo;

-- Vista para resúmen de ventas por cliente
CREATE OR REPLACE VIEW VISTA_VENTAS_CLIENTES AS
SELECT 
    c.id_cliente,
    c.nombre,
    COUNT(v.id_venta) AS total_ventas,
    NVL(SUM(v.total), 0) AS total_gastado
FROM CLIENTES c
LEFT JOIN VENTAS v ON c.id_cliente = v.id_cliente
GROUP BY c.id_cliente, c.nombre;

-- Vista para notificaciones no leídas
CREATE OR REPLACE VIEW VISTA_NOTIFICACIONES_NO_LEIDAS AS
SELECT 
    n.id_notificacion,
    n.id_usuario,
    n.id_producto,
    n.tipo,
    n.mensaje,
    n.fecha_creacion
FROM NOTIFICACIONES n
WHERE n.leida = 0;

-- Vista para estado de caja
CREATE OR REPLACE VIEW VISTA_ESTADO_CAJA AS
SELECT 
    c.id_caja,
    c.id_usuario,
    c.fecha,
    c.saldo_inicial,
    c.ingresos,
    c.egresos,
    c.saldo_final
FROM CAJA c;