-- Crear tabla VENTAS
CREATE TABLE VENTAS (
    id_venta NUMBER(10) CONSTRAINT pk_ventas PRIMARY KEY,
    id_cliente NUMBER(10),
    id_usuario NUMBER(10) NOT NULL,
    fecha_venta DATE DEFAULT SYSDATE,
    total NUMBER(12,2) NOT NULL CHECK (total >= 0),
    estado VARCHAR2(20) DEFAULT 'COMPLETADA' CHECK (estado IN ('PENDIENTE', 'COMPLETADA', 'CANCELADA')),
    tipo_pago VARCHAR2(20) DEFAULT 'EFECTIVO' CHECK (tipo_pago IN ('EFECTIVO', 'TARJETA', 'TRANSFERENCIA')),
    descuento NUMBER(10,2) DEFAULT 0 CHECK (descuento >= 0),
    impuesto NUMBER(10,2) DEFAULT 0 CHECK (impuesto >= 0),
    observaciones VARCHAR2(500)
);

-- Crear secuencia
CREATE SEQUENCE seq_ventas
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_ventas_id
    BEFORE INSERT ON VENTAS
    FOR EACH ROW
BEGIN
    IF :NEW.id_venta IS NULL THEN
        :NEW.id_venta := seq_ventas.NEXTVAL;
    END IF;
END;
/

-- Índices
CREATE INDEX idx_ventas_fecha ON VENTAS(fecha_venta);
CREATE INDEX idx_ventas_cliente ON VENTAS(id_cliente);
CREATE INDEX idx_ventas_usuario ON VENTAS(id_usuario);
CREATE INDEX idx_ventas_estado ON VENTAS(estado);

-- Comentarios
COMMENT ON TABLE VENTAS IS 'Tabla de ventas realizadas';