-- Insertar clientes de prueba
INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Cliente General', NULL, NULL, NULL);

INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Empresa ABC S.A.', '555-0101', 'contacto@empresaabc.com', 'Av. Principal 123, Ciudad');

INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Restaurante El Buen Sabor', '555-0102', 'pedidos@buensabor.com', 'Calle Comercio 456, Centro');

INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Supermercado La Esquina', '555-0103', 'compras@laesquina.com', 'Av. Los Andes 789, Norte');

INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Hotel Plaza', '555-0104', 'administracion@hotelplaza.com', 'Plaza Central 321, Centro');

INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Cafetería Central', '555-0105', 'info@cafeteriacentral.com', 'Calle 5ta 654, Sur');

INSERT INTO CLIENTES (nombre, telefono, email, direccion) VALUES
('Panadería San José', '555-0106', NULL, 'Barrio San José, Mz 12');

COMMIT;

-- Verificar clientes insertados
SELECT id_cliente, nombre, telefono FROM CLIENTES ORDER BY id_cliente;