-- Crear tabla de auditoría para productos
CREATE TABLE AUDITORIA_PRODUCTOS (
    id_auditoria NUMBER(10) PRIMARY KEY,
    id_producto NUMBER(10),
    accion VARCHAR2(10), -- INSERT, UPDATE, DELETE
    campo_modificado VARCHAR2(50),
    valor_anterior VARCHAR2(500),
    valor_nuevo VARCHAR2(500),
    usuario_modificacion VARCHAR2(50),
    fecha_modificacion DATE DEFAULT SYSDATE
);

-- Crear secuencia para auditoría
CREATE SEQUENCE seq_auditoria_productos START WITH 1 INCREMENT BY 1;

-- Trigger de auditoría para productos
CREATE OR REPLACE TRIGGER trg_auditoria_productos
    AFTER INSERT OR UPDATE OR DELETE ON PRODUCTOS
    FOR EACH ROW
DECLARE
    v_accion VARCHAR2(10);
    v_usuario VARCHAR2(50);
BEGIN
    -- Obtener usuario actual
    SELECT USER INTO v_usuario FROM DUAL;
    
    -- Determinar acción
    IF INSERTING THEN
        v_accion := 'INSERT';
        INSERT INTO AUDITORIA_PRODUCTOS (
            id_auditoria, id_producto, accion, campo_modificado, 
            valor_nuevo, usuario_modificacion
        ) VALUES (
            seq_auditoria_productos.NEXTVAL, :NEW.id_producto, v_accion, 
            'REGISTRO_COMPLETO', 'Producto creado: ' || :NEW.nombre, v_usuario
        );
    ELSIF UPDATING THEN
        v_accion := 'UPDATE';
        
        -- Auditar cambios específicos
        IF :OLD.nombre != :NEW.nombre THEN
            INSERT INTO AUDITORIA_PRODUCTOS (
                id_auditoria, id_producto, accion, campo_modificado,
                valor_anterior, valor_nuevo, usuario_modificacion
            ) VALUES (
                seq_auditoria_productos.NEXTVAL, :NEW.id_producto, v_accion,
                'NOMBRE', :OLD.nombre, :NEW.nombre, v_usuario
            );
        END IF;
        
        IF :OLD.stock_minimo != :NEW.stock_minimo THEN
            INSERT INTO AUDITORIA_PRODUCTOS (
                id_auditoria, id_producto, accion, campo_modificado,
                valor_anterior, valor_nuevo, usuario_modificacion
            ) VALUES (
                seq_auditoria_productos.NEXTVAL, :NEW.id_producto, v_accion,
                'STOCK_MINIMO', TO_CHAR(:OLD.stock_minimo), TO_CHAR(:NEW.stock_minimo), v_usuario
            );
        END IF;
        
        IF :OLD.activo != :NEW.activo THEN
            INSERT INTO AUDITORIA_PRODUCTOS (
                id_auditoria, id_producto, accion, campo_modificado,
                valor_anterior, valor_nuevo, usuario_modificacion
            ) VALUES (
                seq_auditoria_productos.NEXTVAL, :NEW.id_producto, v_accion,
                'ESTADO', 
                CASE :OLD.activo WHEN 1 THEN 'ACTIVO' ELSE 'INACTIVO' END,
                CASE :NEW.activo WHEN 1 THEN 'ACTIVO' ELSE 'INACTIVO' END,
                v_usuario
            );
        END IF;
        
    ELSIF DELETING THEN
        v_accion := 'DELETE';
        INSERT INTO AUDITORIA_PRODUCTOS (
            id_auditoria, id_producto, accion, campo_modificado,
            valor_anterior, usuario_modificacion
        ) VALUES (
            seq_auditoria_productos.NEXTVAL, :OLD.id_producto, v_accion,
            'REGISTRO_COMPLETO', 'Producto eliminado: ' || :OLD.nombre, v_usuario
        );
    END IF;
END;
/