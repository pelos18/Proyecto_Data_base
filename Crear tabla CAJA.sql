-- Crear tabla CAJA
CREATE TABLE CAJA (
    id_caja NUMBER(10) CONSTRAINT pk_caja PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL,
    fecha DATE DEFAULT SYSDATE,
    saldo_inicial NUMBER(12,2) DEFAULT 0 CHECK (saldo_inicial >= 0),
    ingresos NUMBER(12,2) DEFAULT 0 CHECK (ingresos >= 0),
    egresos NUMBER(12,2) DEFAULT 0 CHECK (egresos >= 0),
    saldo_final NUMBER(12,2) DEFAULT 0,
    estado VARCHAR2(20) DEFAULT 'ABIERTA' CHECK (estado IN ('ABIERTA', 'CERRADA')),
    fecha_apertura DATE DEFAULT SYSDATE,
    fecha_cierre DATE,
    observaciones VARCHAR2(500)
);

-- Crear secuencia
CREATE SEQUENCE seq_caja
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento y cálculo de saldo final
CREATE OR REPLACE TRIGGER trg_caja_id
    BEFORE INSERT OR UPDATE ON CAJA
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.id_caja IS NULL THEN
        :NEW.id_caja := seq_caja.NEXTVAL;
    END IF;
    -- Calcular saldo final automáticamente
    :NEW.saldo_final := :NEW.saldo_inicial + :NEW.ingresos - :NEW.egresos;
END;
/

-- Índices
CREATE INDEX idx_caja_fecha ON CAJA(fecha);
CREATE INDEX idx_caja_usuario ON CAJA(id_usuario);
CREATE INDEX idx_caja_estado ON CAJA(estado);

-- Comentarios
COMMENT ON TABLE CAJA IS 'Tabla de control de caja diaria';