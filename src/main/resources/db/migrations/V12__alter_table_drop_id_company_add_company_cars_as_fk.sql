ALTER TABLE orders DROP CONSTRAINT fk_company;

ALTER TABLE orders DROP COLUMN id_company;

ALTER TABLE orders ADD COLUMN id_company_cars INT;

ALTER TABLE orders
ADD CONSTRAINT fk_company_car
FOREIGN KEY (id_company_cars) REFERENCES company_cars(id);
