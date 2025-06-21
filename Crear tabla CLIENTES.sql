-- Crear tabla CLIENTES
CREATE TABLE CLIENTES (
    id_cliente NUMBER(10) CONSTRAINT pk_clientes PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    telefono VARCHAR2(20),
    email VARCHAR2(100),
    direccion VARCHAR2(200),
    fecha_registro DATE DEFAULT SYSDATE,
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1))
);

-- Crear secuencia
CREATE SEQUENCE seq_clientes
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_clientes_id
    BEFORE INSERT ON CLIENTES
    FOR EACH ROW
BEGIN
    IF :NEW.id_cliente IS NULL THEN
        :NEW.id_cliente := seq_clientes.NEXTVAL;
    END IF;
END;
/

-- Índices para búsquedas frecuentes
CREATE INDEX idx_clientes_nombre ON CLIENTES(nombre);
CREATE INDEX idx_clientes_telefono ON CLIENTES(telefono);

-- Comentarios
COMMENT ON TABLE CLIENTES IS 'Tabla de clientes';