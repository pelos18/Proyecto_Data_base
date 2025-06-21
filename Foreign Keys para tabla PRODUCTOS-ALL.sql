-- Foreign Keys para tabla PRODUCTOS
ALTER TABLE PRODUCTOS 
ADD CONSTRAINT fk_productos_categoria 
FOREIGN KEY (id_categoria) REFERENCES CATEGORIAS(id_categoria);

ALTER TABLE PRODUCTOS 
ADD CONSTRAINT fk_productos_marca 
FOREIGN KEY (id_marca) REFERENCES MARCAS(id_marca);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_productos_categoria IS 'Relación producto-categoría';
COMMENT ON CONSTRAINT fk_productos_marca IS 'Relación producto-marca';

















-- Foreign Keys para tabla LOTES_INVENTARIO
ALTER TABLE LOTES_INVENTARIO 
ADD CONSTRAINT fk_lotes_producto 
FOREIGN KEY (id_producto) REFERENCES PRODUCTOS(id_producto);

ALTER TABLE LOTES_INVENTARIO 
ADD CONSTRAINT fk_lotes_proveedor 
FOREIGN KEY (id_proveedor) REFERENCES PROVEEDORES(id_proveedor);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_lotes_producto IS 'Relación lote-producto';
COMMENT ON CONSTRAINT fk_lotes_proveedor IS 'Relación lote-proveedor';







-- Foreign Keys para tabla VENTAS
ALTER TABLE VENTAS 
ADD CONSTRAINT fk_ventas_cliente 
FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente);

ALTER TABLE VENTAS 
ADD CONSTRAINT fk_ventas_usuario 
FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_ventas_cliente IS 'Relación venta-cliente';
COMMENT ON CONSTRAINT fk_ventas_usuario IS 'Relación venta-usuario que registra';














-- Foreign Keys para tabla DETALLE_VENTAS
ALTER TABLE DETALLE_VENTAS 
ADD CONSTRAINT fk_detalle_venta 
FOREIGN KEY (id_venta) REFERENCES VENTAS(id_venta) ON DELETE CASCADE;

ALTER TABLE DETALLE_VENTAS 
ADD CONSTRAINT fk_detalle_lote 
FOREIGN KEY (id_lote) REFERENCES LOTES_INVENTARIO(id_lote);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_detalle_venta IS 'Relación detalle-venta (CASCADE para eliminar detalles si se elimina venta)';
COMMENT ON CONSTRAINT fk_detalle_lote IS 'Relación detalle-lote de inventario';















-- Foreign Keys para tabla CAJA
ALTER TABLE CAJA 
ADD CONSTRAINT fk_caja_usuario 
FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_caja_usuario IS 'Relación caja-usuario responsable';

-- Foreign Keys para tabla NOTIFICACIONES
ALTER TABLE NOTIFICACIONES 
ADD CONSTRAINT fk_notif_usuario 
FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario);

ALTER TABLE NOTIFICACIONES 
ADD CONSTRAINT fk_notif_producto 
FOREIGN KEY (id_producto) REFERENCES PRODUCTOS(id_producto);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_notif_usuario IS 'Relación notificación-usuario destinatario';
COMMENT ON CONSTRAINT fk_notif_producto IS 'Relación notificación-producto (opcional)';












-- Script para verificar que todas las Foreign Keys se crearon correctamente
SELECT 
    c.constraint_name,
    c.table_name,
    c.r_constraint_name,
    p.table_name as referenced_table,
    c.status,
    c.validated
FROM user_constraints c
JOIN user_constraints p ON c.r_constraint_name = p.constraint_name
WHERE c.constraint_type = 'R'
ORDER BY c.table_name, c.constraint_name;

-- Verificar columnas de las Foreign Keys
SELECT 
    cc.constraint_name,
    cc.table_name,
    cc.column_name,
    cc.position
FROM user_cons_columns cc
JOIN user_constraints c ON cc.constraint_name = c.constraint_name
WHERE c.constraint_type = 'R'
ORDER BY cc.table_name, cc.constraint_name, cc.position;









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



























