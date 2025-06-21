-- Índices compuestos para consultas frecuentes

-- Para consultas de inventario por producto y fecha
CREATE INDEX idx_lotes_producto_fecha 
ON LOTES_INVENTARIO(id_producto, fecha_ingreso);

-- Para consultas de ventas por fecha y usuario
CREATE INDEX idx_ventas_fecha_usuario 
ON VENTAS(fecha_venta, id_usuario);

-- Para consultas de productos por categoría y marca
CREATE INDEX idx_productos_cat_marca 
ON PRODUCTOS(id_categoria, id_marca);

-- Para consultas de stock disponible
CREATE INDEX idx_lotes_disponible 
ON LOTES_INVENTARIO(id_producto, cantidad_disponible) 
WHERE activo = 1 AND cantidad_disponible > 0;

-- Para consultas de productos próximos a caducar
CREATE INDEX idx_lotes_caducidad_activos 
ON LOTES_INVENTARIO(fecha_caducidad, id_producto) 
WHERE activo = 1 AND cantidad_disponible > 0;

-- Para notificaciones no leídas por usuario
CREATE INDEX idx_notif_usuario_no_leidas 
ON NOTIFICACIONES(id_usuario, fecha_creacion) 
WHERE leida = 0;

-- Para búsquedas de texto en productos
CREATE INDEX idx_productos_nombre_upper 
ON PRODUCTOS(UPPER(nombre));

-- Para búsquedas de clientes por nombre
CREATE INDEX idx_clientes_nombre_upper 
ON CLIENTES(UPPER(nombre));