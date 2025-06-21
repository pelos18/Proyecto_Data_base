-- Insertar usuarios de prueba
INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES
('admin', 'admin123', 'Administrador Principal', 'ADMIN', 1);

INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES
('supervisor1', 'super123', 'Juan Carlos Supervisor', 'SUPERVISOR', 1);

INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES
('vendedor1', 'vend123', 'María González', 'VENDEDOR', 1);

INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES
('vendedor2', 'vend123', 'Carlos Rodríguez', 'VENDEDOR', 1);

INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES
('vendedor3', 'vend123', 'Ana Martínez', 'VENDEDOR', 0);

COMMIT;

-- Verificar usuarios insertados
SELECT id_usuario, usuario, nombre, rol, activo FROM USUARIOS ORDER BY id_usuario;