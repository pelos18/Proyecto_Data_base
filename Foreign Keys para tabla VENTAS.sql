-- Foreign Keys para tabla VENTAS
ALTER TABLE VENTAS 
ADD CONSTRAINT fk_ventas_cliente 
FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente);

ALTER TABLE VENTAS 
ADD CONSTRAINT fk_ventas_usuario 
FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_ventas_cliente IS 'Relación venta-cliente';
COMMENT ON CONSTRAINT fk_ventas_usuario IS 'Relación venta-usuario que registra';