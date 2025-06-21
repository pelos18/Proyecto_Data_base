-- Crear tabla NOTIFICACIONES
CREATE TABLE NOTIFICACIONES (
    id_notificacion NUMBER(10) CONSTRAINT pk_notificaciones PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL,
    id_producto NUMBER(10),
    tipo VARCHAR2(50) NOT NULL CHECK (tipo IN ('STOCK_BAJO', 'CADUCIDAD_PROXIMA', 'PRODUCTO_CADUCADO', 'VENTA_REALIZADA', 'SISTEMA')),
    mensaje VARCHAR2(500) NOT NULL,
    fecha_creacion DATE DEFAULT SYSDATE,
    leida NUMBER(1) DEFAULT 0 CHECK (leida IN (0,1)),
    prioridad VARCHAR2(10) DEFAULT 'MEDIA' CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'CRITICA')),
    fecha_lectura DATE
);

-- Crear secuencia
CREATE SEQUENCE seq_notificaciones
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- Crear trigger para auto-incremento
CREATE OR REPLACE TRIGGER trg_notificaciones_id
    BEFORE INSERT ON NOTIFICACIONES
    FOR EACH ROW
BEGIN
    IF :NEW.id_notificacion IS NULL THEN
        :NEW.id_notificacion := seq_notificaciones.NEXTVAL;
    END IF;
END;
/

-- Trigger para marcar fecha de lectura
CREATE OR REPLACE TRIGGER trg_notificaciones_lectura
    BEFORE UPDATE ON NOTIFICACIONES
    FOR EACH ROW
BEGIN
    IF :NEW.leida = 1 AND :OLD.leida = 0 THEN
        :NEW.fecha_lectura := SYSDATE;
    END IF;
END;
/

-- Índices
CREATE INDEX idx_notif_usuario ON NOTIFICACIONES(id_usuario);
CREATE INDEX idx_notif_producto ON NOTIFICACIONES(id_producto);
CREATE INDEX idx_notif_tipo ON NOTIFICACIONES(tipo);
CREATE INDEX idx_notif_leida ON NOTIFICACIONES(leida);
CREATE INDEX idx_notif_fecha ON NOTIFICACIONES(fecha_creacion);

-- Comentarios
COMMENT ON TABLE NOTIFICACIONES IS 'Tabla de notificaciones del sistema';
COMMENT ON COLUMN NOTIFICACIONES.tipo IS 'Tipos: STOCK_BAJO, CADUCIDAD_PROXIMA, PRODUCTO_CADUCADO, VENTA_REALIZADA, SISTEMA';