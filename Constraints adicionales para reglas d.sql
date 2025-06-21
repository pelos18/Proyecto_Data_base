-- Constraints adicionales para reglas de negocio

-- En LOTES_INVENTARIO: cantidad_disponible no puede ser mayor que cantidad
ALTER TABLE LOTES_INVENTARIO 
ADD CONSTRAINT chk_cantidad_disponible 
CHECK (cantidad_disponible <= cantidad);

-- En LOTES_INVENTARIO: precio_venta debe ser mayor que precio_compra
ALTER TABLE LOTES_INVENTARIO 
ADD CONSTRAINT chk_precio_venta_mayor 
CHECK (precio_venta >= precio_compra);

-- En VENTAS: total debe ser mayor que 0 para ventas completadas
ALTER TABLE VENTAS 
ADD CONSTRAINT chk_total_completada 
CHECK (estado != 'COMPLETADA' OR total > 0);

-- En DETALLE_VENTAS: subtotal debe coincidir con el cálculo
-- (Este se maneja con trigger, pero agregamos constraint de respaldo)
ALTER TABLE DETALLE_VENTAS 
ADD CONSTRAINT chk_subtotal_positivo 
CHECK (subtotal >= 0);

-- En CAJA: no puede haber dos cajas abiertas el mismo día para el mismo usuario
CREATE UNIQUE INDEX idx_caja_usuario_fecha_abierta 
ON CAJA(id_usuario, fecha) 
WHERE estado = 'ABIERTA';

-- En USUARIOS: el usuario debe tener al menos 3 caracteres
ALTER TABLE USUARIOS 
ADD CONSTRAINT chk_usuario_longitud 
CHECK (LENGTH(usuario) >= 3);

-- En PRODUCTOS: stock_minimo no puede ser negativo
ALTER TABLE PRODUCTOS 
ADD CONSTRAINT chk_stock_minimo_positivo 
CHECK (stock_minimo >= 0);