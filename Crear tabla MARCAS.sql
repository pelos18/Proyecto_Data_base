-- Crear tabla MARCAS
CREATE TABLE MARCAS (
    id_marca NUMBER(10) CONSTRAINT pk_marcas PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL UNIQUE,
    descripcion VARCHAR2(200),
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1)),
    fecha_creacion DATE DEFAULT SYSDATE
);

-- Crear secuencia
CREATE SEQUENCE seq_marcas
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_marcas_id
    BEFORE INSERT ON MARCAS
    FOR EACH ROW
BEGIN
    IF :NEW.id_marca IS NULL THEN
        :NEW.id_marca := seq_marcas.NEXTVAL;
    END IF;
END;
/

-- Comentarios
COMMENT ON TABLE MARCAS IS 'Tabla de marcas de productos';