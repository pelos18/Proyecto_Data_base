-- Crear tabla LOTES_INVENTARIO
CREATE TABLE LOTES_INVENTARIO (
    id_lote NUMBER(10) CONSTRAINT pk_lotes_inventario PRIMARY KEY,
    id_producto NUMBER(10) NOT NULL,
    id_proveedor NUMBER(10) NOT NULL,
    cantidad NUMBER(10) NOT NULL CHECK (cantidad >= 0),
    cantidad_disponible NUMBER(10) NOT NULL CHECK (cantidad_disponible >= 0),
    precio_compra NUMBER(10,2) NOT NULL CHECK (precio_compra >= 0),
    precio_venta NUMBER(10,2) NOT NULL CHECK (precio_venta >= 0),
    fecha_ingreso DATE DEFAULT SYSDATE,
    fecha_caducidad DATE,
    lote_proveedor VARCHAR2(50),
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1))
);

-- Crear secuencia
CREATE SEQUENCE seq_lotes_inventario
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_lotes_inventario_id
    BEFORE INSERT ON LOTES_INVENTARIO
    FOR EACH ROW
BEGIN
    IF :NEW.id_lote IS NULL THEN
        :NEW.id_lote := seq_lotes_inventario.NEXTVAL;
    END IF;
    -- Inicializar cantidad disponible igual a cantidad
    IF :NEW.cantidad_disponible IS NULL THEN
        :NEW.cantidad_disponible := :NEW.cantidad;
    END IF;
END;
/

-- Índices
CREATE INDEX idx_lotes_producto ON LOTES_INVENTARIO(id_producto);
CREATE INDEX idx_lotes_proveedor ON LOTES_INVENTARIO(id_proveedor);
CREATE INDEX idx_lotes_caducidad ON LOTES_INVENTARIO(fecha_caducidad);

-- Comentarios
COMMENT ON TABLE LOTES_INVENTARIO IS 'Tabla de lotes de inventario por producto';
COMMENT ON COLUMN LOTES_INVENTARIO.cantidad_disponible IS 'Cantidad disponible para venta';