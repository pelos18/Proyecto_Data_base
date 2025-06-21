-- Ver qué triggers están activos/inactivos
SELECT 
    trigger_name,
    table_name,
    status,
    triggering_event
FROM user_triggers 
WHERE trigger_name LIKE 'TRG_%'
ORDER BY table_name, trigger_name;