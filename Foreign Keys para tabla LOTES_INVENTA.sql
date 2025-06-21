-- Foreign Keys para tabla LOTES_INVENTARIO
ALTER TABLE LOTES_INVENTARIO 
ADD CONSTRAINT fk_lotes_producto 
FOREIGN KEY (id_producto) REFERENCES PRODUCTOS(id_producto);

ALTER TABLE LOTES_INVENTARIO 
ADD CONSTRAINT fk_lotes_proveedor 
FOREIGN KEY (id_proveedor) REFERENCES PROVEEDORES(id_proveedor);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_lotes_producto IS 'Relación lote-producto';
COMMENT ON CONSTRAINT fk_lotes_proveedor IS 'Relación lote-proveedor';