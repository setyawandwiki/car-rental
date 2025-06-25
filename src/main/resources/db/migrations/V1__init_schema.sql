CREATE TABLE countries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    id_country INT,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_country FOREIGN KEY (id_country) REFERENCES countries(id)
);

CREATE TABLE genders (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255),
    id_city INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_city FOREIGN KEY (id_city) REFERENCES cities(id)
);

CREATE TABLE roles(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    birth_date DATE,
    email VARCHAR(255),
    phone_number VARCHAR(255),
    username VARCHAR(255),
    password VARCHAR(255),
    id_gender INT,
    id_address INT,
    id_role INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_gender FOREIGN KEY (id_gender) REFERENCES genders(id),
    CONSTRAINT fk_address FOREIGN KEY (id_address) REFERENCES address(id),
    CONSTRAINT fk_roles FOREIGN KEY (id_role) REFERENCES roles(id)
);

CREATE TABLE car_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE status (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    seats VARCHAR(255),
    baggages INT,
    year INT, -- ubah kolom 'year' yang tadinya pakai tanda kutip!
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    rate INT,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE company_cars (
    id BIGSERIAL PRIMARY KEY,
    id_company INT,
    id_car INT,
    price INT,
    id_car_type INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_car_types FOREIGN KEY (id_car_type) REFERENCES car_types (id),
    CONSTRAINT fk_car FOREIGN KEY (id_car) REFERENCES cars(id),
    CONSTRAINT fk_company FOREIGN KEY (id_company) REFERENCES companies(id)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    pickup_loc VARCHAR(255),
    dropoff_loc VARCHAR(255),
    price_total INT,
    id_company INT,
    id_user INT,
    id_status INT,
    pickup_date DATE,
    dropoff_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id),
    CONSTRAINT fk_company FOREIGN KEY (id_company) REFERENCES companies(id),
    CONSTRAINT fk_status FOREIGN KEY (id_status) REFERENCES status(id)
);