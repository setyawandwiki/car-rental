ALTER TABLE genders
   DROP CONSTRAINT fk_city;

ALTER TABLE genders
    DROP COLUMN id_city;

ALTER TABLE orders
ADD COLUMN id_status INT,
ADD CONSTRAINT fk_status
FOREIGN KEY (id_status) REFERENCES status(id);
