-- Foreign Keys para tabla CAJA
ALTER TABLE CAJA 
ADD CONSTRAINT fk_caja_usuario 
FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario);

-- Comentarios sobre las relaciones
COMMENT ON CONSTRAINT fk_caja_usuario IS 'Relación caja-usuario responsable';