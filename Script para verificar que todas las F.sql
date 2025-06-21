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