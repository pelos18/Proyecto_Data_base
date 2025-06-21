-- Insertar categorías
INSERT INTO CATEGORIAS (nombre, descripcion) VALUES
('Bebidas', 'Bebidas alcohólicas y no alcohólicas');

INSERT INTO CATEGORIAS (nombre, descripcion) VALUES
('Alimentos', 'Productos alimenticios en general');

INSERT INTO CATEGORIAS (nombre, descripcion) VALUES
('Lácteos', 'Leche, quesos, yogurt y derivados');

INSERT INTO CATEGORIAS (nombre, descripcion) VALUES
('Carnes', 'Carnes rojas, blancas y embutidos');

INSERT INTO CATEGORIAS (nombre, descripcion) VALUES
('Limpieza', 'Productos de limpieza y aseo');

INSERT INTO CATEGORIAS (nombre, descripcion) VALUES
('Panadería', 'Pan, pasteles y productos de panadería');

-- Insertar marcas
INSERT INTO MARCAS (nombre) VALUES ('Coca-Cola');
INSERT INTO MARCAS (nombre) VALUES ('Pepsi');
INSERT INTO MARCAS (nombre) VALUES ('Nestlé');
INSERT INTO MARCAS (nombre) VALUES ('Bimbo');
INSERT INTO MARCAS (nombre) VALUES ('Alpina');
INSERT INTO MARCAS (nombre) VALUES ('Colanta');
INSERT INTO MARCAS (nombre) VALUES ('Zenú');
INSERT INTO MARCAS (nombre) VALUES ('Fabuloso');
INSERT INTO MARCAS (nombre) VALUES ('Ariel');
INSERT INTO MARCAS (nombre) VALUES ('Genérica');

COMMIT;

-- Verificar categorías y marcas
SELECT 'CATEGORIAS' as tipo, id_categoria as id, nombre FROM CATEGORIAS
UNION ALL
SELECT 'MARCAS' as tipo, id_marca as id, nombre FROM MARCAS
ORDER BY tipo, id;