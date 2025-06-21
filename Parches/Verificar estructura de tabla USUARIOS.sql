-- Verificar estructura de tabla USUARIOS
SELECT 'USUARIOS' as tabla, column_name, data_type, nullable
FROM user_tab_columns 
WHERE table_name = 'USUARIOS'
ORDER BY column_id;

-- Verificar estructura de tabla VENTAS
SELECT 'VENTAS' as tabla, column_name, data_type, nullable
FROM user_tab_columns 
WHERE table_name = 'VENTAS'
ORDER BY column_id;

-- Verificar estructura de tabla DETALLE_VENTAS
SELECT 'DETALLE_VENTAS' as tabla, column_name, data_type, nullable
FROM user_tab_columns 
WHERE table_name = 'DETALLE_VENTAS'
ORDER BY column_id;

-- Verificar estructura de tabla CAJA
SELECT 'CAJA' as tabla, column_name, data_type, nullable
FROM user_tab_columns 
WHERE table_name = 'CAJA'
ORDER BY column_id;

-- Verificar estructura de tabla LOTES_INVENTARIO
SELECT 'LOTES_INVENTARIO' as tabla, column_name, data_type, nullable
FROM user_tab_columns 
WHERE table_name = 'LOTES_INVENTARIO'
ORDER BY column_id;

-- Contar columnas por tabla
SELECT 
    table_name,
    COUNT(*) as total_columnas
FROM user_tab_columns 
WHERE table_name IN ('USUARIOS', 'CLIENTES', 'PROVEEDORES', 'CATEGORIAS', 'MARCAS',
                     'PRODUCTOS', 'LOTES_INVENTARIO', 'VENTAS', 'DETALLE_VENTAS', 
                     'CAJA', 'NOTIFICACIONES')
GROUP BY table_name
ORDER BY table_name;