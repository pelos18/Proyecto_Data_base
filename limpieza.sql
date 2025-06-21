-- 1. LIMPIEZA TOTAL DE LAS TABLAS
DELETE FROM DETALLE_VENTAS;
DELETE FROM VENTAS;
DELETE FROM CAJA;
DELETE FROM NOTIFICACIONES;
DELETE FROM LOTES_INVENTARIO;
DELETE FROM PRODUCTOS;
DELETE FROM CLIENTES;
DELETE FROM PROVEEDORES;
DELETE FROM MARCAS;
DELETE FROM CATEGORIAS;
DELETE FROM USUARIOS;
COMMIT;

-- 2. REINICIO DE TODAS LAS SECUENCIAS
DECLARE
  PROCEDURE reset_seq(p_seq_name IN VARCHAR2) IS
    l_val NUMBER;
  BEGIN
    EXECUTE IMMEDIATE 'SELECT ' || p_seq_name || '.NEXTVAL FROM DUAL' INTO l_val;
    EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_seq_name || ' INCREMENT BY -' || l_val || ' MINVALUE 0';
    EXECUTE IMMEDIATE 'SELECT ' || p_seq_name || '.NEXTVAL FROM DUAL' INTO l_val;
    EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_seq_name || ' INCREMENT BY 1 MINVALUE 0';
  END;
BEGIN
  reset_seq('SEQ_USUARIOS'); reset_seq('SEQ_CLIENTES'); reset_seq('SEQ_PROVEEDORES'); reset_seq('SEQ_CATEGORIAS');
  reset_seq('SEQ_MARCAS'); reset_seq('SEQ_PRODUCTOS'); reset_seq('SEQ_LOTES_INVENTARIO'); reset_seq('SEQ_VENTAS');
  reset_seq('SEQ_DETALLE_VENTAS'); reset_seq('SEQ_CAJA'); reset_seq('SEQ_NOTIFICACIONES');
END;
/

-- 3. SIMULACIÓN DE SESIÓN DE USUARIO
BEGIN
    dbms_session.set_identifier('1');
END;
/

-- 4. INSERCIÓN DE DATOS DE PRUEBA
INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES ('jefe', 'pass_jefe', 'Jefe de Tienda', 'dueño', 1);
INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES ('vendedor', 'pass_vend', 'Vendedor Uno', 'vendedor', 1);
INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Lácteos', 'Productos derivados de la leche');
INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Botanas', 'Snacks y aperitivos');
INSERT INTO MARCAS (nombre) VALUES ('Lala');
INSERT INTO MARCAS (nombre) VALUES ('Sabritas');
INSERT INTO PROVEEDORES (nombre, telefono) VALUES ('Distribuidor Central', '5511223344');
INSERT INTO PRODUCTOS (codigo_barras, nombre, id_categoria, id_marca, stock_minimo, activo) VALUES ('750100', 'Leche Entera 1L', 1, 1, 20, 1);
INSERT INTO PRODUCTOS (codigo_barras, nombre, id_categoria, id_marca, stock_minimo, activo) VALUES ('750200', 'Papas Fritas 170g', 2, 2, 30, 1);
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta) VALUES (1, 1, 100, 20.00, 25.00);
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta) VALUES (2, 1, 200, 28.00, 45.00);
COMMIT;