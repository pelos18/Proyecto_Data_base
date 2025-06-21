-- Modifica la columna para que acepte fecha y hora con alta precisión
ALTER TABLE VENTAS MODIFY (fecha_venta TIMESTAMP);

-- Actualiza el valor por defecto para que use la fecha y hora actual con alta precisión
ALTER TABLE VENTAS MODIFY (fecha_venta DEFAULT SYSTIMESTAMP);