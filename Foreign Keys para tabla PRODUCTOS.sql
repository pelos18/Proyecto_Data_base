-- Foreign Keys para tabla PRODUCTOS
ALTER TABLE PRODUCTOS 
ADD CONSTRAINT fk_productos_categoria 
FOREIGN KEY (id_categoria) REFERENCES CATEGORIAS(id_categoria);

ALTER TABLE PRODUCTOS 
ADD CONSTRAINT fk_productos_marca 
FOREIGN KEY (id_marca) REFERENCES MARCAS(id_marca);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_productos_categoria IS 'Relación producto-categoría';
COMMENT ON CONSTRAINT fk_productos_marca IS 'Relación producto-marca';