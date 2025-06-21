-- Crear tabla CATEGORIAS
CREATE TABLE CATEGORIAS (
    id_categoria NUMBER(10) CONSTRAINT pk_categorias PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL UNIQUE,
    descripcion VARCHAR2(200),
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1)),
    fecha_creacion DATE DEFAULT SYSDATE
);

-- Crear secuencia
CREATE SEQUENCE seq_categorias
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_categorias_id
    BEFORE INSERT ON CATEGORIAS
    FOR EACH ROW
BEGIN
    IF :NEW.id_categoria IS NULL THEN
        :NEW.id_categoria := seq_categorias.NEXTVAL;
    END IF;
END;
/

-- Comentarios
COMMENT ON TABLE CATEGORIAS IS 'Tabla de categorías de productos';