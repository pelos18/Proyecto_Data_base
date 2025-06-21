-- =================================================================
-- SCRIPT DEFINITIVO DE LIMPIEZA Y CARGA (CON REINICIO TOTAL)
-- =================================================================

-- 1. LIMPIEZA TOTAL DE LAS TABLAS
-- -----------------------------------------------------------------
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

-- 2. REINICIO DE TODAS LAS SECUENCIAS (ESTA ES LA CORRECCIÓN)
-- -----------------------------------------------------------------
DECLARE
  v_last_val NUMBER;
  PROCEDURE reset_seq(p_seq_name IN VARCHAR2) IS
    l_val NUMBER;
  BEGIN
    EXECUTE IMMEDIATE 'SELECT ' || p_seq_name || '.NEXTVAL FROM DUAL' INTO l_val;
    EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_seq_name || ' INCREMENT BY -' || l_val || ' MINVALUE 0';
    EXECUTE IMMEDIATE 'SELECT ' || p_seq_name || '.NEXTVAL FROM DUAL' INTO l_val;
    EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_seq_name || ' INCREMENT BY 1 MINVALUE 0';
  END;
BEGIN
  reset_seq('SEQ_USUARIOS');
  reset_seq('SEQ_CLIENTES');
  reset_seq('SEQ_PROVEEDORES');
  reset_seq('SEQ_CATEGORIAS');
  reset_seq('SEQ_MARCAS');
  reset_seq('SEQ_PRODUCTOS');
  reset_seq('SEQ_LOTES_INVENTARIO');
  reset_seq('SEQ_VENTAS');
  reset_seq('SEQ_DETALLE_VENTAS');
  reset_seq('SEQ_CAJA');
  reset_seq('SEQ_NOTIFICACIONES');
END;
/


-- 3. SIMULACIÓN DE SESIÓN DE USUARIO
-- -----------------------------------------------------------------
BEGIN
    dbms_session.set_identifier('1');
END;
/


-- 4. INSERCIÓN DE DATOS (AHORA LOS IDS COINCIDIRÁN)
-- -----------------------------------------------------------------
INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES ('jefe', 'pass_jefe', 'Jefe de Tienda', 'dueño', 1);
INSERT INTO USUARIOS (usuario, password, nombre, rol, activo) VALUES ('gerente', 'pass_gerente', 'Gerente General', 'administrativo', 1);
INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Lácteos', 'Productos derivados de la leche');
INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Limpieza', 'Productos para la limpieza del hogar');
INSERT INTO CATEGORIAS (nombre, descripcion) VALUES ('Botanas', 'Snacks y aperitivos');
INSERT INTO MARCAS (nombre) VALUES ('Lala');
INSERT INTO MARCAS (nombre) VALUES ('Fabuloso');
INSERT INTO MARCAS (nombre) VALUES ('Sabritas');
INSERT INTO PROVEEDORES (nombre, telefono) VALUES ('Distribuidor Central de Lácteos', '5511223344');
INSERT INTO PROVEEDORES (nombre, telefono) VALUES ('ProClean S.A. de C.V.', '5522334455');
INSERT INTO PROVEEDORES (nombre, telefono) VALUES ('Pepsico México', '5533445566');
INSERT INTO PRODUCTOS (codigo_barras, nombre, id_categoria, id_marca, stock_minimo, activo) VALUES ('750102030405', 'Leche Entera 1L', 1, 1, 20, 1);
INSERT INTO PRODUCTOS (codigo_barras, nombre, id_categoria, id_marca, stock_minimo, activo) VALUES ('750203040506', 'Limpiador Multiusos Lavanda 1L', 2, 2, 15, 1);
INSERT INTO PRODUCTOS (codigo_barras, nombre, id_categoria, id_marca, stock_minimo, activo) VALUES ('750304050607', 'Papas Fritas Sal y Limón 170g', 3, 3, 30, 1);
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_caducidad) VALUES (1, 1, 100, 20.00, 25.00, TRUNC(SYSDATE) + 30);
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_caducidad) VALUES (2, 2, 50, 30.00, 38.00, TRUNC(SYSDATE) + 365);
INSERT INTO LOTES_INVENTARIO (id_producto, id_proveedor, cantidad, precio_compra, precio_venta, fecha_caducidad) VALUES (3, 3, 200, 28.00, 45.00, TRUNC(SYSDATE) + 90);
INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos) VALUES (1, TRUNC(SYSDATE), 0, 0, 0);


-- 5. CONFIRMACIÓN FINAL
-- -----------------------------------------------------------------
COMMIT;


-- 6. VERIFICACIÓN FINAL (OPCIONAL)
-- -----------------------------------------------------------------
SELECT * FROM USUARIOS;
SELECT * FROM CATEGORIAS;
SELECT * FROM MARCAS;
SELECT * FROM PROVEEDORES;
SELECT p.id_producto, p.nombre, c.nombre AS categoria, m.nombre AS marca FROM PRODUCTOS p JOIN CATEGORIAS c ON p.id_categoria = c.id_categoria JOIN MARCAS m ON p.id_marca = m.id_marca;
SELECT li.id_lote, p.nombre AS producto, li.cantidad, li.precio_venta FROM LOTES_INVENTARIO li JOIN PRODUCTOS p ON li.id_producto = p.id_producto;
SELECT * FROM CAJA;