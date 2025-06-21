-- Crear tabla PRODUCTOS
CREATE TABLE PRODUCTOS (
    id_producto NUMBER(10) CONSTRAINT pk_productos PRIMARY KEY,
    codigo_barras VARCHAR2(50) UNIQUE,
    nombre VARCHAR2(100) NOT NULL,
    descripcion VARCHAR2(500),
    id_categoria NUMBER(10) NOT NULL,
    id_marca NUMBER(10) NOT NULL,
    stock_minimo NUMBER(10) DEFAULT 0,
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1)),
    fecha_creacion DATE DEFAULT SYSDATE,
    fecha_modificacion DATE DEFAULT SYSDATE
);

-- Crear secuencia
CREATE SEQUENCE seq_productos
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_productos_id
    BEFORE INSERT ON PRODUCTOS
    FOR EACH ROW
BEGIN
    IF :NEW.id_producto IS NULL THEN
        :NEW.id_producto := seq_productos.NEXTVAL;
    END IF;
END;
/

-- Índices para búsquedas frecuentes
CREATE INDEX idx_productos_nombre ON PRODUCTOS(nombre);
CREATE INDEX idx_productos_codigo ON PRODUCTOS(codigo_barras);
CREATE INDEX idx_productos_categoria ON PRODUCTOS(id_categoria);
CREATE INDEX idx_productos_marca ON PRODUCTOS(id_marca);

-- Comentarios
COMMENT ON TABLE PRODUCTOS IS 'Tabla de productos del inventario';
COMMENT ON COLUMN PRODUCTOS.stock_minimo IS 'Umbral para alerta de stock bajo';