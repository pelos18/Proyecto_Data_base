-- Insertar registros de caja
-- Caja del vendedor María González (hace 5 días)
INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final) VALUES
(3, SYSDATE-5, 50000.00, 13900.00, 2000.00, 61900.00);

-- Caja del vendedor Carlos Rodríguez (hace 4 días)
INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final) VALUES
(4, SYSDATE-4, 60000.00, 42200.00, 1500.00, 100700.00);

-- Caja del vendedor María González (hace 3 días)
INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final) VALUES
(3, SYSDATE-3, 61900.00, 95700.00, 3000.00, 154600.00);

-- Caja del vendedor Carlos Rodríguez (hace 2 días)
INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final) VALUES
(4, SYSDATE-2, 100700.00, 116900.00, 2500.00, 215100.00);

-- Caja del vendedor María González (hoy)
INSERT INTO CAJA (id_usuario, fecha, saldo_inicial, ingresos, egresos, saldo_final) VALUES
(3, SYSDATE, 154600.00, 34400.00, 1000.00, 188000.00);

COMMIT;

-- Verificar cajas insertadas
SELECT 
    c.id_caja,
    u.nombre as cajero,
    TO_CHAR(c.fecha, 'DD/MM/YYYY') as fecha,
    c.saldo_inicial,
    c.ingresos,
    c.egresos,
    c.saldo_final
FROM CAJA c
JOIN USUARIOS u ON c.id_usuario = u.id_usuario
ORDER BY c.fecha DESC;