CREATE TABLE roles(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);


ALTER TABLE cities ADD CONSTRAINT unique_city UNIQUE (name);

INSERT INTO countries(id, name) VALUES (1, 'Indonesia');
INSERT INTO cities(id_country, name) VALUES (1, 'Bogor'),(1, 'Jakarta'),(1, 'Bekasi');