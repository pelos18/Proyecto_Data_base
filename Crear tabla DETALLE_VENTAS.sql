-- Crear tabla DETALLE_VENTAS
CREATE TABLE DETALLE_VENTAS (
    id_detalle NUMBER(10) CONSTRAINT pk_detalle_ventas PRIMARY KEY,
    id_venta NUMBER(10) NOT NULL,
    id_lote NUMBER(10) NOT NULL,
    cantidad NUMBER(10) NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMBER(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMBER(12,2) NOT NULL CHECK (subtotal >= 0),
    descuento_item NUMBER(10,2) DEFAULT 0 CHECK (descuento_item >= 0)
);

-- Crear secuencia
CREATE SEQUENCE seq_detalle_ventas
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento y cálculo de subtotal
CREATE OR REPLACE TRIGGER trg_detalle_ventas_id
    BEFORE INSERT OR UPDATE ON DETALLE_VENTAS
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.id_detalle IS NULL THEN
        :NEW.id_detalle := seq_detalle_ventas.NEXTVAL;
    END IF;
    -- Calcular subtotal automáticamente
    :NEW.subtotal := (:NEW.cantidad * :NEW.precio_unitario) - :NEW.descuento_item;
END;
/

-- Índices
CREATE INDEX idx_detalle_venta ON DETALLE_VENTAS(id_venta);
CREATE INDEX idx_detalle_lote ON DETALLE_VENTAS(id_lote);

-- Comentarios
COMMENT ON TABLE DETALLE_VENTAS IS 'Detalle de productos vendidos por venta';