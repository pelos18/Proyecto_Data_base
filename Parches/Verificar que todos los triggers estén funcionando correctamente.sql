-- Verificar que todos los triggers estén funcionando correctamente
SELECT 
    trigger_name,
    table_name,
    status,
    triggering_event
FROM user_triggers 
WHERE trigger_name LIKE 'TRG_%'
AND status = 'ENABLED'
ORDER BY table_name, trigger_name;

-- Verificar si hay errores en los triggers
SELECT 
    name as trigger_name,
    type,
    line,
    position,
    text as error_message
FROM user_errors 
WHERE type = 'TRIGGER'
AND name LIKE 'TRG_%';

-- Si no hay errores, debería mostrar "no rows selected"