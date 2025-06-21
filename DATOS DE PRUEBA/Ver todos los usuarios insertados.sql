-- Ver todos los usuarios insertados
SELECT 
    id_usuario,
    usuario,
    nombre,
    rol,
    activo
FROM USUARIOS
ORDER BY id_usuario;