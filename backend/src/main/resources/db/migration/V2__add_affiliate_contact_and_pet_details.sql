ALTER TABLE afiliado
    ADD COLUMN celular VARCHAR(20),
    ADD COLUMN correo VARCHAR(254),
    ADD COLUMN direccion VARCHAR(200);

ALTER TABLE mascota
    ADD COLUMN raza VARCHAR(100),
    ADD COLUMN edad INTEGER,
    ADD COLUMN color VARCHAR(50),
    ADD CONSTRAINT ck_mascota_edad_range CHECK (edad IS NULL OR edad BETWEEN 0 AND 100);