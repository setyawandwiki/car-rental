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
    id_role INT,
    account_number VARCHAR(50),
    bank_code VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_gender FOREIGN KEY (id_gender) REFERENCES genders(id),
    CONSTRAINT fk_roles FOREIGN KEY (id_role) REFERENCES roles(id)
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255),
    id_city INT,
    status VARCHAR(50),
    id_user BIGSERIAL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_city FOREIGN KEY (id_city) REFERENCES cities(id),
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id)
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
    year INT,
    image VARCHAR(255),
    location int,
    CONSTRAINT fk_location FOREIGN KEY (location) REFERENCES cities(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    id_user BIGSERIAL,
    name VARCHAR(255),
    rate INT,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id),
    updated_at TIMESTAMP
);

CREATE TABLE company_cars (
    id BIGSERIAL PRIMARY KEY,
    id_company INT,
    id_car INT,
    price INT,
    id_car_type INT UNIQUE,
    status VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_car_types FOREIGN KEY (id_car_type) REFERENCES car_types (id),
    CONSTRAINT fk_car FOREIGN KEY (id_car) REFERENCES cars(id),
    CONSTRAINT fk_company FOREIGN KEY (id_company) REFERENCES companies(id)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    pickup_loc VARCHAR(255),
    drop_off_loc VARCHAR(255),
    price_total INT,
    id_company_cars INT,
    id_user INT,
    status VARCHAR(50),
    pickup_date DATE,
    drop_off_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id),
    CONSTRAINT fk_company FOREIGN KEY (id_company_cars) REFERENCES company_cars(id)
);

CREATE TABLE vendor_balance (
    id SERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL UNIQUE,
    available_balance BIGINT DEFAULT 0,
    pending_withdrawal BIGINT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vendor_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE transaction_ledgers(
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    amount BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_company_ledger FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE platform_profits(
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    company_id INT NOT NULL,
    profit_amount INT NOT NULL,
    percentage INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_profit FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_company_profit FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE payments(
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL UNIQUE,
    invoice_id VARCHAR(100) UNIQUE NOT NULL,
    external_id VARCHAR(100) UNIQUE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    amount INT NOT NULL,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_payment FOREIGN KEY (order_id) REFERENCES orders(id)
);

INSERT INTO roles (name) VALUES ('USER'),('ADMIN');

INSERT INTO countries(name) VALUES ('Indonesia');

INSERT INTO cities (id_country, name) VALUES (1, 'Bogor'),
(1, 'Jakarta'),
(1, 'Cibinong'),
(1, 'Cibubur');

INSERT INTO car_types (name) VALUES ('Automatic'), ('Manual');