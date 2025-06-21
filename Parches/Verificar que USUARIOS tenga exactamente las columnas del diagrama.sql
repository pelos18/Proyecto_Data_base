-- Verificar que USUARIOS tenga exactamente las columnas del diagrama
SELECT 
    CASE 
        WHEN COUNT(*) = 5 THEN '✓ USUARIOS - Estructura correcta'
        ELSE '✗ USUARIOS - Estructura incorrecta. Columnas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'USUARIOS'
AND column_name IN ('ID_USUARIO', 'USUARIO', 'PASSWORD', 'NOMBRE', 'ROL', 'ACTIVO');

-- Verificar que VENTAS tenga exactamente las columnas del diagrama
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ VENTAS - Estructura correcta'
        ELSE '✗ VENTAS - Estructura incorrecta. Columnas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'VENTAS'
AND column_name IN ('ID_VENTA', 'ID_CLIENTE', 'ID_USUARIO', 'FECHA_VENTA', 'TOTAL', 'ESTADO');

-- Verificar que DETALLE_VENTAS tenga exactamente las columnas del diagrama
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ DETALLE_VENTAS - Estructura correcta'
        ELSE '✗ DETALLE_VENTAS - Estructura incorrecta. Columnas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'DETALLE_VENTAS'
AND column_name IN ('ID_DETALLE', 'ID_VENTA', 'ID_LOTE', 'CANTIDAD', 'PRECIO_UNITARIO', 'SUBTOTAL');

-- Verificar que LOTES_INVENTARIO tenga exactamente las columnas del diagrama
SELECT 
    CASE 
        WHEN COUNT(*) = 8 THEN '✓ LOTES_INVENTARIO - Estructura correcta'
        ELSE '✗ LOTES_INVENTARIO - Estructura incorrecta. Columnas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'LOTES_INVENTARIO'
AND column_name IN ('ID_LOTE', 'ID_PRODUCTO', 'ID_PROVEEDOR', 'CANTIDAD', 'PRECIO_COMPRA', 'PRECIO_VENTA', 'FECHA_INGRESO', 'FECHA_CADUCIDAD');

-- Verificar que CAJA tenga exactamente las columnas del diagrama
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ CAJA - Estructura correcta'
        ELSE '✗ CAJA - Estructura incorrecta. Columnas: ' || COUNT(*)
    END as resultado
FROM user_tab_columns 
WHERE table_name = 'CAJA'
AND column_name IN ('ID_CAJA', 'ID_USUARIO', 'FECHA', 'SALDO_INICIAL', 'INGRESOS', 'EGRESOS', 'SALDO_FINAL');