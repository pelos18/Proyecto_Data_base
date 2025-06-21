-- Foreign Keys para tabla DETALLE_VENTAS
ALTER TABLE DETALLE_VENTAS 
ADD CONSTRAINT fk_detalle_venta 
FOREIGN KEY (id_venta) REFERENCES VENTAS(id_venta) ON DELETE CASCADE;

ALTER TABLE DETALLE_VENTAS 
ADD CONSTRAINT fk_detalle_lote 
FOREIGN KEY (id_lote) REFERENCES LOTES_INVENTARIO(id_lote);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_detalle_venta IS 'Relación detalle-venta (CASCADE para eliminar detalles si se elimina venta)';
COMMENT ON CONSTRAINT fk_detalle_lote IS 'Relación detalle-lote de inventario';