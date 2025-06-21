-- Verificación corregida según el diagrama ER original
-- USUARIOS debe tener 6 columnas (incluyendo ACTIVO)
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ USUARIOS - Estructura correcta (6 columnas)'
        ELSE '✗ USUARIOS - Estructura incorrecta. Columnas encontradas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'USUARIOS'
AND column_name IN ('ID_USUARIO', 'USUARIO', 'PASSWORD', 'NOMBRE', 'ROL', 'ACTIVO');

-- VENTAS debe tener 6 columnas
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ VENTAS - Estructura correcta (6 columnas)'
        ELSE '✗ VENTAS - Estructura incorrecta. Columnas encontradas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'VENTAS'
AND column_name IN ('ID_VENTA', 'ID_CLIENTE', 'ID_USUARIO', 'FECHA_VENTA', 'TOTAL', 'ESTADO');

-- LOTES_INVENTARIO debe tener 8 columnas
SELECT 
    CASE 
        WHEN COUNT(*) = 8 THEN '✓ LOTES_INVENTARIO - Estructura correcta (8 columnas)'
        ELSE '✗ LOTES_INVENTARIO - Estructura incorrecta. Columnas encontradas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'LOTES_INVENTARIO'
AND column_name IN ('ID_LOTE', 'ID_PRODUCTO', 'ID_PROVEEDOR', 'CANTIDAD', 'PRECIO_COMPRA', 'PRECIO_VENTA', 'FECHA_INGRESO', 'FECHA_CADUCIDAD');

-- DETALLE_VENTAS debe tener 6 columnas
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ DETALLE_VENTAS - Estructura correcta (6 columnas)'
        ELSE '✗ DETALLE_VENTAS - Estructura incorrecta. Columnas encontradas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'DETALLE_VENTAS'
AND column_name IN ('ID_DETALLE', 'ID_VENTA', 'ID_LOTE', 'CANTIDAD', 'PRECIO_UNITARIO', 'SUBTOTAL');

-- NOTIFICACIONES debe tener 7 columnas
SELECT 
    CASE 
        WHEN COUNT(*) = 7 THEN '✓ NOTIFICACIONES - Estructura correcta (7 columnas)'
        ELSE '✗ NOTIFICACIONES - Estructura incorrecta. Columnas encontradas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'NOTIFICACIONES'
AND column_name IN ('ID_NOTIFICACION', 'ID_USUARIO', 'ID_PRODUCTO', 'TIPO', 'MENSAJE', 'FECHA_CREACION', 'LEIDA');