-- Crear tabla PROVEEDORES
CREATE TABLE PROVEEDORES (
    id_proveedor NUMBER(10) CONSTRAINT pk_proveedores PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    telefono VARCHAR2(20),
    direccion VARCHAR2(200),
    email VARCHAR2(100),
    contacto VARCHAR2(100),
    fecha_registro DATE DEFAULT SYSDATE,
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1))
);

-- Crear secuencia
CREATE SEQUENCE seq_proveedores
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_proveedores_id
    BEFORE INSERT ON PROVEEDORES
    FOR EACH ROW
BEGIN
    IF :NEW.id_proveedor IS NULL THEN
        :NEW.id_proveedor := seq_proveedores.NEXTVAL;
    END IF;
END;
/

-- Índices
CREATE INDEX idx_proveedores_nombre ON PROVEEDORES(nombre);

-- Comentarios
COMMENT ON TABLE PROVEEDORES IS 'Tabla de proveedores';