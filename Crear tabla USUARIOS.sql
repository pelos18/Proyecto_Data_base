-- Crear tabla USUARIOS
CREATE TABLE USUARIOS (
    id_usuario NUMBER(10) CONSTRAINT pk_usuarios PRIMARY KEY,
    usuario VARCHAR2(50) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    rol VARCHAR2(20) NOT NULL CHECK (rol IN ('ADMIN', 'VENDEDOR', 'SUPERVISOR')),
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1)),
    fecha_creacion DATE DEFAULT SYSDATE,
    fecha_modificacion DATE DEFAULT SYSDATE
);

-- Crear secuencia para auto-incremento
CREATE SEQUENCE seq_usuarios
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_usuarios_id
    BEFORE INSERT ON USUARIOS
    FOR EACH ROW
BEGIN
    IF :NEW.id_usuario IS NULL THEN
        :NEW.id_usuario := seq_usuarios.NEXTVAL;
    END IF;
END;
/

-- Comentarios
COMMENT ON TABLE USUARIOS IS 'Tabla de usuarios del sistema';
COMMENT ON COLUMN USUARIOS.rol IS 'Roles: ADMIN, VENDEDOR, SUPERVISOR';
COMMENT ON COLUMN USUARIOS.activo IS '1=Activo, 0=Inactivo';