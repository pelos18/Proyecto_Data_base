-- Verificar estado actual de la base de datos
SELECT 'TABLAS CREADAS' AS tipo, COUNT(*) AS cantidad
FROM user_tables 
WHERE table_name IN ('USUARIOS', 'CLIENTES', 'PROVEEDORES', 'CATEGORIAS', 'MARCAS', 
                     'PRODUCTOS', 'LOTES_INVENTARIO', 'VENTAS', 'DETALLE_VENTAS', 
                     'CAJA', 'NOTIFICACIONES')
UNION ALL
SELECT 'FOREIGN KEYS CREADAS' AS tipo, COUNT(*) AS cantidad
FROM user_constraints 
WHERE constraint_type = 'R'
UNION ALL
SELECT 'SECUENCIAS CREADAS' AS tipo, COUNT(*) AS cantidad
FROM user_sequences
WHERE sequence_name LIKE 'SEQ_%'
UNION ALL
SELECT 'TRIGGERS CREADOS' AS tipo, COUNT(*) AS cantidad
FROM user_triggers
WHERE trigger_name LIKE 'TRG_%'
UNION ALL
SELECT 'INDICES CREADOS' AS tipo, COUNT(*) AS cantidad
FROM user_indexes
WHERE index_name LIKE 'IDX_%';

-- Verificar Foreign Keys específicas
SELECT 
    constraint_name,
    table_name,
    r_constraint_name,
    status
FROM user_constraints 
WHERE constraint_type = 'R'
ORDER BY table_name;