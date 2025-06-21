-- Insertar proveedores de prueba
INSERT INTO PROVEEDORES (nombre, telefono, direccion) VALUES
('Distribuidora Nacional', '555-1001', 'Zona Industrial Norte, Bodega 15');

INSERT INTO PROVEEDORES (nombre, telefono, direccion) VALUES
('Alimentos Frescos S.A.', '555-1002', 'Mercado Central, Local 45-50');

INSERT INTO PROVEEDORES (nombre, telefono, direccion) VALUES
('Bebidas y Más', '555-1003', 'Av. Industrial 2500, Sector B');

INSERT INTO PROVEEDORES (nombre, telefono, direccion) VALUES
('Productos de Limpieza Total', '555-1004', 'Calle Industria 100, Zona Este');

INSERT INTO PROVEEDORES (nombre, telefono, direccion) VALUES
('Lácteos La Vaca Feliz', '555-1005', 'Carretera Norte Km 25, Granja');

INSERT INTO PROVEEDORES (nombre, telefono, direccion) VALUES
('Carnes Premium', '555-1006', 'Frigorífico Central, Nave 3');

COMMIT;

-- Verificar proveedores insertados
SELECT id_proveedor, nombre, telefono FROM PROVEEDORES ORDER BY id_proveedor;